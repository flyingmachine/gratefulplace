(ns gratefulplace.models.favorite
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils))

(defn create!
  [post_id user_id]
  (insert e/favorite (values {:post_id post_id :user_id user_id})))

(defn destroy!
  [post_id user_id]
  (delete e/favorite
          (where
           (str->int
            {:post_id post_id :user_id user_id}
            :post_id :user_id))))

(defn all
  [user_id]
  (select e/favorite
          (fields :user_id :post_id)
          (with e/post)
          (where {:user_id (str->int user_id)})))