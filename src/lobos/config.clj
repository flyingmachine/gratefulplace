(ns lobos.config
  (:use lobos.connectivity))

(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :user "daniel"
   :password ""
   :subname "//localhost:5432/gratefulplace-development"})

(open-global db)