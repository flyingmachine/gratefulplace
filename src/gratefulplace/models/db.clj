(ns gratefulplace.models.db
  (:use korma.db))


;; TODO figure out how to put this config in lein and read it from
;; there
;; google "lein full project map"
(defdb db (postgres {:db "gratefulplace-development"
                     :user "daniel"
                     :password ""
                     ;;OPTIONAL KEYS
                     :host "localhost"}))