(ns gratefulplace.controllers.users
  (:require [gratefulplace.models.user :as user]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.users :as view]
            [ring.util.response :as res]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows])

  (:use [gratefulplace.controllers.common :only (if-valid view)]
        gratefulplace.models.permissions
        gratefulplace.paths))


(defn show-new
  [params]
  (view view/show-new))

(defn user-from-params
  [params]
  (user/for-user-page params))

(defn create!
  [req]
  (let [{:keys [uri request-method params]} req]
    (when (and (= uri "/users")
               (= request-method :post))
      (if-valid
       params
       (:create user/validation-contexts)
       errors

       (workflows/make-auth (user/create! params))
       {:body (view view/show-new :errors errors)}))))

(defn show
  [params]
  (view
   view/show
   :user (user-from-params params)))

(defn posts
  [params]
  (let [user (user-from-params params)]
    (view
     view/posts
     :user  user
     :posts (post/all (korma.core/where {:user_id (:id user)})))))

(defn comments
  [params]
  (let [user (user-from-params params)]
    (view
     view/comments
     :user user
     :comments (comment/all (korma.core/where {:user_id (:id user)})))))

(defn edit
  [params]
  (let [username (:username params)]
    (protect
     (can-modify-profile? username)
     (view
      view/edit
      :user (user/one {:username username})))))

(defn update
  [params]
  (let [username (:username params)]
    (protect
     (can-modify-profile? username)
     (let [validations (cond
                        (:change-password params)
                        ((:change-password user/validation-contexts)
                         (let [user (user/one {:username username})] (:password user)))
                        
                        (:email params)
                        (:update-email user/validation-contexts)
                        
                        :else {})]
       
       (if-valid
        params validations errors
        (let [new-attributes (if (:change-password params)
                               {:password (get-in params [:change-password :new-password])}
                               (dissoc params :username))]
          (user/update! {:username username} new-attributes)
          (res/redirect (user-edit-path username "?success=true")))
        (view
         view/edit
         :user params
         :errors errors))))))

(defn update-notification-settings
  [params]
  (println "this thing!")
  (let [username (:username params)]
    (protect
     (can-modify-profile? username)
     (let [new-attributes (merge {:receive_comment_notifications false
                                  :receive_newsletter false}
                                 (dissoc params :username))]
          (user/update! {:username username} new-attributes)
          (res/redirect (user-edit-path username "?success=true#change-notifications"))))))