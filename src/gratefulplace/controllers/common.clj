(ns gratefulplace.controllers.common
  (:require [cemerick.friend :as friend])
  (:use gratefulplace.utils
        gratefulplace.models.permissions
        flyingmachine.webutils.controllers))

(defview view
  {:current-auth '(cemerick.friend/current-authentication)
   :errors {}
   :params 'params})


(defmacro with-visibility
  "this conditional was used a few times so I put it in a macro"
  [current-auth {:keys [moderator logged-in not-logged-in]}]
  `(let [current-auth# ~current-auth]
     (cond (moderator? (:username current-auth#)) ~moderator
           current-auth# ~logged-in
           :else ~not-logged-in)))