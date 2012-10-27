(ns gratefulplace.controllers.users
  (:require [gratefulplace.models.user :as user]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.users :as view]
            [ring.util.response :as res]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows])

  (:use [gratefulplace.controllers.common :only (if-valid)]))


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
  (let [username (:username params)
        user (user/one {:username username})
        vals (cond
              (:change-password params) (select-keys validations [:change-password])
              (:email params) (select-keys validations [:email])
              :else {})]
    
    (if-valid
     params vals errors
     (do
       (user/update! username (dissoc params :username))
       (res/redirect (str "/users/" username "/edit?success=true")))
     (view/edit params errors))))