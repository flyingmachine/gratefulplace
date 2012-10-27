(ns gratefulplace.controllers.users
  (:require [gratefulplace.models.user :as user]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.users :as view]
            [ring.util.response :as res]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows])

  (:use [gratefulplace.controllers.common :only (if-valid)])
  (:import org.mindrot.jbcrypt.BCrypt))


(def validations
  {:username
   ["Your username must be between 4 and 24 characters long"
    #(and
      (>= (count %) 4)
      (<= (count %) 24))
    "That username is already taken"
    #(not (user/one {:username %}))]
   
   :password
   ["Your password must be at least 4 characters long"
    #(>= (count %) 4)]
   
   :change-password
   ["Your passwords do not match"
    #(= (:new-password %) (:new-password-confirmation %))]
  
   :email
   ["You must enter a valid email address"
    #(and
      (not-empty %)
      (re-find #"@" %))]})

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
     (select-keys validations [:username :password :email])
     errors
     
     (workflows/make-auth (user/create! params))
     {:body (view/show-new params errors)})))

(defn edit
  [username]
  (let [user (user/one {:username username})]
    (view/edit user nil)))

;; TODO don't really need to have a redirect here do I?
(defn update
  [params]
  (println params)
  (let [username (:username params)
        user (user/one {:username username})
        vals (cond
              ;; TODO I have to do this funky stuff so that I can
              ;; actually access current password value for user
              (:change-password params)
              (update-in
               (select-keys validations [:change-password])
               [:change-password]
               conj
               "You didn't enter the correct value for your current password"
               #(BCrypt/checkpw (:current-password %) (:password user)))
              
              (:email params) (select-keys validations [:email])
              
              :else {})]
    
    (if-valid
     params vals errors
     (do
       (let [new-attributes (dissoc params :username)
             new-attributes (if (:change-password new-attributes)
                              {:password (get-in new-attributes [:change-password :new-password])})]
         (user/update! {:username username} new-attributes))
       (res/redirect (str "/users/" username "/edit?success=true")))
     (view/edit params errors))))