(ns gratefulplace.models.db
  (:use korma.db))

;; TODO figure out how to put this config in lein and read it from
;; there
;; google "lein full project map"
(def db-config
  (if (System/getenv "DATABASE_URL")
    (let [db-uri (java.net.URI. (System/getenv "DATABASE_URL"))
          user-and-password (clojure.string/split (.getUserInfo db-uri) #":")]
      {:classname "org.postgresql.Driver"
       :subprotocol "postgresql"
       :user (get user-and-password 0)
       :password (get user-and-password 1) ; may be nil
       :subname (if (= -1 (.getPort db-uri))
                  (format "//%s%s" (.getHost db-uri) (.getPath db-uri))
                  (format "//%s:%s%s" (.getHost db-uri) (.getPort db-uri) (.getPath db-uri)))})
    (postgres
     {:db "gratefulplace-development"
      :user "daniel"
      :password ""
      ;;OPTIONAL KEYS
      :host "localhost"})))

(defdb db db-config)