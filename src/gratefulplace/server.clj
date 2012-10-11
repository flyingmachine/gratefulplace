(ns gratefulplace.server
  (:require compojure.route
            compojure.handler
            [net.cgrand.enlive-html :as h]
            [gratefulplace.controllers.posts :as posts])
  (:use [ring.adapter.jetty :only (run-jetty)]
        [ring.middleware.params :only (wrap-params)]
        [compojure.core :only (GET PUT POST defroutes)]))

;; templates
;; (h/defsnippet footer "gratefulplace/templates/footer.html" [:.footer]
;;   [message]
;;   [:.footer] (h/content message))

(defroutes app*
  (compojure.route/files "/" {:root "public"})
  (GET "/" [] (posts/list))
  (compojure.route/not-found "Sorry, there's nothing here."))

(def app (compojure.handler/api app*))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))