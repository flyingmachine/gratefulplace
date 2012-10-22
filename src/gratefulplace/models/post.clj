(ns gratefulplace.models.post
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils))

(defn create!
  [attributes]
  (insert e/post (values (assoc attributes :user_id 1))))

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
                 (with e/comment)
                 (where {:id (str->int id)}))))