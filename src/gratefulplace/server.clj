(ns gratefulplace.server
  (:use [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        ring.middleware.keyword-params
        [gratefulplace.middleware.auth :only (auth)]
        [gratefulplace.middleware.routes :only (routes)]))

(def app
  (-> routes
      auth
      wrap-keyword-params
      wrap-params))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))
