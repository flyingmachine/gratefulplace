(ns gratefulplace.models.comment
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.models.entities))

(defn create!
  [attributes]
  (insert e/comment (values (assoc attributes :user_id 1))))

(defn all
  []
  (select e/comment
          (with e/user
                (fields :username))
          (order :created_on :DESC)))