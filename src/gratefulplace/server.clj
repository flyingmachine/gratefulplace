(ns gratefulplace.server
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.posts :as posts]
            [korma.db :as db])
  (:use [ring.adapter.jetty :only (run-jetty)]
        [compojure.core :only (GET PUT POST defroutes)]))

;; templates
;; (h/defsnippet footer "gratefulplace/templates/footer.html" [:.footer]
;;   [message]
;;   [:.footer] (h/content message))

(defroutes app*
  (compojure.route/files "/" {:root "public"})
  (GET "/" [] (posts/all))
  (compojure.route/not-found "Sorry, there's nothing here."))

(def app (compojure.handler/api app*))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))

(db/defdb dev (db/postgres {:db "gratefulplace-development"
                            :user "daniel"
                            :password ""}))