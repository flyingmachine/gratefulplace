(ns gratefulplace.models.post
  (:require gratefulplace.models.db
            [gratefulplace.models.user :as user])
  
  (:use korma.core))

(defentity posts
  (belongs-to user/users))

(defn create!
  [attributes]
  (insert posts (values (assoc attributes :user_id 1))))

(defn all
  []
  (select posts
          (with user/users
                (fields :username))))