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

(def ^:private counter (atom 0))
(def ^:private mappings (ref {}))
(def posts
  [{:author        "Terrence Blowfish"
    :date          "Jan 03, 2012 9:53am"
    :content       "Today I'm grateful for the trees turning colors"
    :comment-count 10}
   {:author        "Mickey Parkplace"
    :date          "Jan 03, 2012 10:12am"
    :content       "This morning I feel grateful for the food in my kitchen"
    :comment-count 3}
   {:author        "Jeremy McWilder"
    :date          "Jan 03, 2012 10:14am"
    :content       "I'm grateful for the cat in my lap"
    :comment-count 0}])

(defn comments [post]
  (if (zero? (:comment-count post))
    "Add a comment"
    (str (:comment-count post) " comments")))
;; returns a seq of strings. to concat, use
;; (apply str (friends-list ...))
(h/deftemplate home "gratefulplace/templates/index.html"
  []
  [:.post] (h/clone-for [post posts]
                        [:.author]   (h/content (:author post))
                        [:.date]     (h/content (:date post))
                        [:.content]  (h/content (:content post))
                        [:.comments] (h/content (comments post))
                        (h/content "This is enlive content")))

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