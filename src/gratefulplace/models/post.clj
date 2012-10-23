(ns gratefulplace.models.post
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils))

(defn create!
  [attributes]
  (insert e/post (values attributes)))

(defn all
  []
  (select e/post
          (with e/user
                (fields :username))
          (order :created_on :DESC)))

(defn by-id
  [id]
  (first (select e/post
                 (with e/user
                       (fields :username))
                 (with e/comment
                       (with e/user
                             fields :username))
                 (where {:id (str->int id)}))))