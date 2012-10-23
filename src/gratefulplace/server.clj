(ns gratefulplace.server
  (:use [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        ring.middleware.keyword-params
        ring.middleware.session
        ring.middleware.session.cookie
        [gratefulplace.middleware.auth :only (auth)]
        [gratefulplace.middleware.routes :only (routes)]))

(def app
  (-> routes
      auth
      (wrap-session {:cookie-name "gratefulplace-session" :store (cookie-store)})
      wrap-keyword-params
      wrap-params))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))
