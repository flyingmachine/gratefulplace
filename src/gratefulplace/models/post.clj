(ns gratefulplace.models.post

  (:use korma.core))

(defentity posts)

(defn create!
  [attributes]
  (insert posts (values (assoc attributes :user_id 1))))