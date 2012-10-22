(ns gratefulplace.models.comment
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils
        gratefulplace.models.entities))

(defn create!
  [attributes]
  (insert e/comment (values (assoc attributes :user_id 1))))

(defn all
  [conditions]
  (select e/comment
          (with e/user
                (fields :username))
          (where (str->int conditions :post_id :user_id))
          (order :created_on :DESC)))