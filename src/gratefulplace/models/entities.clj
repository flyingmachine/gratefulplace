(ns gratefulplace.models.entities
  (:refer-clojure :exclude [comment])
  (:require (cemerick.friend [credentials :as creds]))
  (:use korma.core
        gratefulplace.utils))

(declare user comment)
(defentity post
  (belongs-to user)
  (has-many comment))

(defentity user
  (has-many post)
  (has-many comment)

  ;; todo move user param transform function here
  (prepare #(merge %
                   {:password (creds/hash-bcrypt (:password %))
                    :roles (str [:user])}))
  (transform #(deserialize % :roles)))

(defentity comment
  (belongs-to user)
  (belongs-to post)

  (prepare #(str->int % :post_id)))