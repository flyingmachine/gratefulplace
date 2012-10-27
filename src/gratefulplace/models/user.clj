(ns gratefulplace.models.user
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils))

(defn create!
  [attributes]
  ;; not sure why insert returns serialized roles
  (deserialize
   (insert e/user (values attributes))
   :roles))

(defn update!
  [conditions attributes]
  (update e/user
          (set-fields attributes)
          (where conditions)))

(defn one
  [conditions]
  (first (select e/user
                 (where conditions)
                 (limit 1))))

(defn for-user-page
  [conditions]
  (first (select e/user
                 (where conditions)
                 (with e/post
                       (aggregate (count :*) :count))
                 (with e/comment
                       (aggregate (count :*) :count)))))