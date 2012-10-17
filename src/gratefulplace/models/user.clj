(ns gratefulplace.models.user
  (:require gratefulplace.models.db)
  (:use korma.core))

(defentity users)

(defn create
  [attributes]
  (insert users (values attributes)))