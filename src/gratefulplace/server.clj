(ns gratefulplace.server
  (:use [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        [gratefulplace.middleware.auth :only (auth)]
        [gratefulplace.middleware.routes :only (routes)]))

;; templates
;; (h/defsnippet footer "gratefulplace/templates/footer.html" [:.footer]
;;   [message]
;;   [:.footer] (h/content message))



(def app
  (-> routes
      wrap-params
      auth))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))
