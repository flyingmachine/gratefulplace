(ns gratefulplace.controllers.users
  (:require [gratefulplace.models.user :as user]
            [gratefulplace.views.users :as view]
            [ring.util.response :as res]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]))



(defn show-new
  []
  (view/show-new))

(defn register [{:keys [uri request-method params]}]
  (when (and (= uri "/users")
             (= request-method :post))
    (if (and (:username params) (:password params))
      (workflows/make-auth (user/create! params)))))

(defn create!
  [attributes]
  (user/create! attributes)
  (res/redirect "/"))