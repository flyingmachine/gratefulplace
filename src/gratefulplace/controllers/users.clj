(ns gratefulplace.controllers.users
  (:require [gratefulplace.models.user :as user]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.users :as view]
            [ring.util.response :as res]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows])

  (:use [gratefulplace.controllers.common :only (if-valid)]
        gratefulplace.models.permissions))


(defn show-new
  []
  (view/show-new nil nil))

(defn show
  [username]
  (view/show (user/for-user-page {:username username})))

(defn posts
  [username]
  (let [user (user/for-user-page {:username username})]
    (view/posts
     user
     (post/all {:user_id (:id user)}))))

(defn comments
  [username]
  (let [user (user/for-user-page {:username username})]
    (view/comments
     user
     (comment/all {:user_id (:id user)}))))

(defn create! [{:keys [uri request-method params]}]
  (when (and (= uri "/users")
             (= request-method :post))
    (if-valid
     params
     (:create user/validation-contexts)
     errors
     
     (workflows/make-auth (user/create! params))
     {:body (view/show-new params errors)})))

(defn edit
  [username]
  (protect
   (can-modify-profile? username)
   (let [user (user/one {:username username})]
     (view/edit user nil))))

;; TODO don't really need to have a redirect here do I?
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
          (res/redirect (str "/users/" username "/edit?success=true")))
        (view/edit params errors))))))