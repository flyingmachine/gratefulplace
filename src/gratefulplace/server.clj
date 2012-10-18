(ns gratefulplace.server
  (:require compojure.route
            compojure.handler
            gratefulplace.models.db
            [gratefulplace.controllers.posts :as posts]
            [gratefulplace.controllers.users :as users]
            [gratefulplace.models.user :as user]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds]
                             [openid :as openid]))
  (:use [ring.adapter.jetty :only (run-jetty)]
        [compojure.core :only (GET PUT POST defroutes)]))

;; templates
;; (h/defsnippet footer "gratefulplace/templates/footer.html" [:.footer]
;;   [message]
;;   [:.footer] (h/content message))

(defroutes app*
  (compojure.route/files "/" {:root "public"})
  (GET  "/" [] (posts/all))
  (GET  "/posts/new" [] (posts/new))
  (POST "/posts" [content] (posts/create! {:content content}))
  (GET  "/users/new" [] (users/new))
  (POST "/users" [username email] (users/create! {:username username :email email}))
  (compojure.route/not-found "Sorry, there's nothing here."))

(defn credential-fn
  [username])

(defn session-store-authorize [{:keys [uri request-method params session]}]
  (when (nil? (:cemerick.friend/identity session))
    (if-let [username false]
      (workflows/make-auth (user/one {:username username})))))

(def app
  (-> (compojure.handler/api app*)
      (friend/authenticate 
       {:credential-fn (partial creds/bcrypt-credential-fn credential-fn)
        :workflows [(workflows/interactive-form), gratefulplace.controllers.users/register, session-store-authorize]
        :login-uri "/login"
        :unauthorized-redirect-uri "/login" 
        :default-landing-uri "/"})))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))
