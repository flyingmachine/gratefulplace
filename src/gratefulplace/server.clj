(ns gratefulplace.server
  (:use [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        ring.middleware.keyword-params
        ring.middleware.nested-params
        ring.middleware.session
        gratefulplace.middleware.db-session-store
        [gratefulplace.middleware.auth :only (auth)]
        [gratefulplace.middleware.routes :only (routes)]))

(def app
  (-> routes
      auth
      (wrap-session {:cookie-name "gratefulplace-session" :store (db-session-store)})
      wrap-keyword-params
      wrap-nested-params
      wrap-params))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))
