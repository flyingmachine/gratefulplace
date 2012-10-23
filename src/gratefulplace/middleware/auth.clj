(ns gratefulplace.middleware.auth
  (:require [gratefulplace.controllers.users :as users]
            [gratefulplace.models.user :as user]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds]
                             [openid :as openid])))

(defn credential-fn
  [username]
  (println (user/one {:username username}))
  (user/one {:username username}))

(defn session-store-authorize [{:keys [uri request-method params session]}]
  (when (nil? (:cemerick.friend/identity session))
    (if-let [username (get-in session [:cemerick.friend/identity :current])]
      (workflows/make-auth (user/one {:username username})))))

(defn auth
  [ring-app]
  (friend/authenticate
   ring-app
   {:credential-fn (partial creds/bcrypt-credential-fn credential-fn)
    :workflows [(workflows/interactive-form), users/register, session-store-authorize]
    :login-uri "/session"
    :unauthorized-redirect-uri "/session/new" 
    :default-landing-uri "/"}))