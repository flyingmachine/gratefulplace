(ns gratefulplace.server
  (:require compojure.route
            compojure.handler
            [net.cgrand.enlive-html :as h])
  (:use [ring.adapter.jetty :only (run-jetty)]
        [ring.middleware.params :only (wrap-params)]
        [compojure.core :only (GET PUT POST defroutes)]))

;; templates
;; (h/defsnippet footer "gratefulplace/templates/footer.html" [:.footer]
;;   [message]
;;   [:.footer] (h/content message))


;; returns a seq of strings. to concat, use
;; (apply str (friends-list ...))
(h/deftemplate home "gratefulplace/templates/index.html"
  []
  [:.post :.content] (h/content "This is enlive content"))

(def ^:private counter (atom 0))
(def ^:private mappings (ref {}))

(defn url-for
  [id]
  (@mappings id))

(defn shorten!
  "Stores the given URL under a new unique identifier, or the given identifier
if provided. Returns the identifier as a string.
Modifies the global mapping accordingly."
  ([url]
     (let [id (swap! counter inc)
           id (Long/toString id 36)]
       (or (shorten! url id)
           (recur url))))
  ([url id]
     (dosync
      (when-not (@mappings id)
        (alter mappings assoc id url)
        id))))

(defn retain
  [& [url id :as args]]
  (if-let [id (apply shorten! args)]
    {:status 201
     :headers {"Location" id}
     :body (list "URL " url " assigned the short identifier " id)}
    {:status 409 :body (format "Short URL %s is already taken" id)}))

(defn redirect
  [id]
  (if-let [url (url-for id)]
    (ring.util.response/redirect url)
    {:status 404 :body (str "No such short URL: " id)}))

(defroutes app*
  (compojure.route/files "/" {:root "public"})
  (GET "/" [] (home))
  (PUT "/:id" [id url] (retain url id))
  (POST "/" [url] (retain url))
  (GET "/:id" [id] (redirect id))
  (GET "/list/" [] (interpose "\n" (keys @mappings)))
  (compojure.route/not-found "Sorry, there's nothing here."))

(def app (compojure.handler/api app*))

(defn -main
  []
  (run-jetty #'app {:port 8080 :join? false}))