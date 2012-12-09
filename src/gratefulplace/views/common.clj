(ns gratefulplace.views.common
  (:require [net.cgrand.enlive-html :as h]
            [gratefulplace.models.favorite :as favorite]
            markdown)
  (use [cemerick.friend :only (current-authentication)]
       gratefulplace.utils
       gratefulplace.models.permissions))

(defonce template-dir "gratefulplace/templates/")

(h/defsnippet nav (str template-dir "index.html") [:nav]
  [logged-in]
  [:.auth] (if logged-in
             (h/do-> (h/content "Log Out")
                     (h/set-attr :href "/logout"))
             (h/do-> (h/content "Log In / Register")
                     (h/set-attr :href "/login"))))

(h/deftemplate layout (str template-dir "index.html")
  [html]
  [:html] (h/substitute html)
  [:nav] (h/substitute (nav (current-authentication)))

  [:nav :ul.secondary :#logged-in :a]
  (if-let [username (:username (current-authentication))]
    (h/do->
     (h/set-attr :href (str "/users/" username)))))

;; Need to come up with better name
;; Bundles together some defsnippet commonalities for user with the
;; layout template
;;
;; TODO destructuring doesn't work in argnames
(defmacro defpage
  [name file [& argnames] & body]
  `(do
     (h/defsnippet ~(symbol (str name "*")) (str template-dir ~file) [:html]
       [~@argnames]
       ~@body)
     (defn ~name
       [{:keys [~@argnames]}]
       (layout (~(symbol (str name "*")) ~@argnames)))))

;; Path stuff
(defn path
  [record url-string prefix & suffixes]
  (str "/"
       (apply str
              (interpose
               "/"
               (into [prefix (or (url-string record) record)] suffixes)))))

(defmacro create-path-fns
  [record-type url-id & suffixes]
  `(do
     ~@(map
        (fn [suffix]
          (let [fn-name-suffix (if suffix
                                 (str "-" suffix "-path")
                                 "-path")
                x (gensym)]
            `(defn ~(symbol (str record-type fn-name-suffix))
               [~x]
               ;; TODO is there a briefer way of doing this?
               ~(if suffix
                  `(path ~x ~url-id ~(str record-type "s") ~suffix)
                  `(path ~x ~url-id ~(str record-type "s"))))))
        (conj suffixes nil))))

(create-path-fns "user" :username "edit" "posts" "comments")
(create-path-fns "post" :id "edit" "destroy")
(create-path-fns "favorite" :id "edit" "destroy")
(create-path-fns "comment" :id "edit" "destroy")

(defn comment-on-post-path
  [post, comment]
  (str (post-path post) "#" (:id comment)))

(defn set-path
  [x fun]
  (h/set-attr :href (fun x)))

(defn- xml-str
 "Like clojure.core/str but escapes < > and &."
 [x]
  (-> x str (.replace "&" "&amp;") (.replace "<" "&lt;") (.replace ">" "&gt;")))

(defn md-content
  [content]
  (let [content (or (:content content) content)]
    (h/html-content (markdown/md-to-html-string (xml-str content)))))

(defn format-error-messages
  [errors]
  (str "<ul>" (apply str (map #(str "<li>" % "</li>") errors)) "</ul>"))

(defn error-content
  [errors, key]
  (if-let [messages (get errors key)]
    (h/html-content (format-error-messages messages))))

(defn relation-count
  [record relation-key]
  (get-in record [relation-key 0 :count] 0))

(defn linked-username
  [record]
  (h/do->
   (h/content (:username record))
   (set-path record user-path)))

(defn timestamp->string
  [timestamp]
  (-> (java.text.SimpleDateFormat. "MMM dd, yyyy hh:mma")
      (.format timestamp)))

(defn timestamp->shortstring
  [timestamp]
  (-> (java.text.SimpleDateFormat. "MMM dd, yyyy")
      (.format timestamp)))

(defn created-on
  [x]
  (timestamp->string (:created_on x)))

(defn created-on-short
  [x]
  (timestamp->shortstring (:created_on x)))

(defmacro keep-when
  [condition]
  `(when ~condition
     #(identity %)))

(defn user-favorites
  [user-id]
  (into #{} (map :post_id (favorite/all user-id))))