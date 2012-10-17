(ns gratefulplace.server
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.posts :as posts]
            [gratefulplace.controllers.users :as users])
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
  (compojure.route/not-found "Sorry, there's nothing here."))

(def app (compojure.handler/api app*))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))

