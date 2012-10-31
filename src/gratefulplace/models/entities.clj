(ns gratefulplace.models.entities
  (:refer-clojure :exclude [comment])
  (:require (cemerick.friend [credentials :as creds]))
  (:use korma.core
        gratefulplace.utils))

(declare user comment)

(defn hidden-text->boolean
  [attributes]
  (let [hidden (= "true" (:hidden attributes))]
        (assoc attributes :hidden hidden)))

(defentity post
  (belongs-to user)
  (has-many comment)
  (prepare hidden-text->boolean))

(defentity user
  (has-many post)
  (has-many comment)

  ;; todo move user param transform function here
  (prepare
   (fn [attributes]
     (-> (if (:password attributes)
           (assoc attributes :password (creds/hash-bcrypt (:password attributes)))
            attributes)
         (assoc :roles (str [:user])))))
  
  (transform #(deserialize % :roles)))

(defentity comment
  (belongs-to user)
  (belongs-to post)

  (prepare
   (fn [attributes]
     (-> attributes
         hidden-text->boolean
         (str->int :post_id)))))

(defentity user_session
  (prepare #(merge % {:data (str (:data %))}))
  (transform #(deserialize % :data)))

(defentity favorite
  (belongs-to post)
  (belongs-to user)
  
  (prepare #(str->int % :post_id :user_id)))