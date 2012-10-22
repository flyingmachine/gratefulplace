(ns gratefulplace.models.entities
  (:refer-clojure :exclude [comment])
  (:use korma.core
        gratefulplace.utils))

(declare user comment)
(defentity post
  (belongs-to user)
  (has-many comment))

(defentity user
  (has-many post)
  (has-many comment))

(defentity comment
  (belongs-to user)
  (belongs-to post)

  (prepare #(assoc % :post_id (str->int (:post_id %)))))