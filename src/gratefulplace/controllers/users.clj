(ns gratefulplace.controllers.users
  (:require [gratefulplace.controllers.common :as common]
            [gratefulplace.models.user :as user]
            [gratefulplace.views.users :as view]
            [ring.util.response :as res]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]))

(def validations
  [[:username
    ["Your username must be between 4 and 24 characters long"
     #(> (count %) 4)
     #(< (count %) 24)]
    ["That username is already taken"
     #(not (user/one {:username %}))]]
   
   [:password
    ["Your password must be at least 4 characters long"
     #(> (count %) 4)]]
   
   [:email
    ["You must enter a valid email address"
     #(not-empty %)
     #(re-find #"@" %)]]])

(defn show-new
  []
  (view/show-new {} []))

(defn register [{:keys [uri request-method params]}]
  (when (and (= uri "/users")
             (= request-method :post))
    (if (and (:username params) (:password params))
      (workflows/make-auth (user/create! params)))))

(defn create!
  [attributes]
  (common/if-valid
   attributes validations errors
   (do (user/create! attributes)
       (res/redirect "/"))
   (show-new attributes errors)))