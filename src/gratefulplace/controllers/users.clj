(ns gratefulplace.controllers.users
  (:require [net.cgrand.enlive-html :as h]
            [gratefulplace.models.user :as user]
            [ring.util.response :as res]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            ))

(h/deftemplate new "gratefulplace/templates/users/new.html"
  [])

(defn register [{:keys [uri request-method params]}]
  (when (and (= uri "/users")
             (= request-method :post))
    (if (and (:username params) (:password params))
      (workflows/make-auth (user/create! params)))))

(defn create!
  [attributes]
  (user/create! attributes)
  (res/redirect "/"))