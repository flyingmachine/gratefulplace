(ns ^{:doc "convenience methods for generating paths and generating path-generating fns"}
    gratefulplace.lib.paths)

(defn path
  [record url-string prefix & suffixes]
  (str "/"
       (apply str
              (interpose
               "/"
               (into [prefix (or (url-string record) record)] suffixes)))))

(defmacro create-path-fns
  [record-type url-id & suffixes]
  "example: 
  (create-path-fns \"user\" :username \"edit\" \"posts\" \"comments\" \"notification-settings\")

expands to:
 (do
 (clojure.core/defn
  user-path
  [G__4013]
  (gratefulplace.views.common/path G__4013 :username \"users\"))
 (clojure.core/defn
  user-edit-path
  [G__4014]
  (gratefulplace.views.common/path G__4014 :username \"users\" \"edit\"))
 (clojure.core/defn
  user-posts-path
  [G__4015]
  (gratefulplace.views.common/path G__4015 :username \"users\" \"posts\"))
 (clojure.core/defn
  user-comments-path
  [G__4016]
  (gratefulplace.views.common/path
   G__4016
   :username
   \"users\"
   \"comments\"))
 (clojure.core/defn
  user-notification-settings-path
  [G__4017]
  (gratefulplace.views.common/path
   G__4017
   :username
   \"users\"
   \"notification-settings\")))"

  `(do
     ~@(map
        (fn [suffix]
          (let [fn-name-suffix (if suffix
                                 (str "-" suffix "-path")
                                 "-path")
                x (gensym)
                y (gensym)
                fn-name (symbol (str record-type fn-name-suffix))]
            `(defn ~fn-name
               ([~x]
                  ;; TODO is there a briefer way of doing this?
                  ;; maybe make a suffix version and non-suffix version
                  ~(if suffix
                     `(path ~x ~url-id ~(str record-type "s") ~suffix)
                     `(path ~x ~url-id ~(str record-type "s"))))
               ([~x & ~y]
                  (str (~fn-name ~x) (apply str ~y))))))
        (conj suffixes nil))))