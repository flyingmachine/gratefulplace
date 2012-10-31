(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema
               config helpers)))

(defmigration add-users-table
  (up []
      (create
       (tbl :user
            (varchar :username 50 :unique)
            (check :username (> (length :username) 1))

            (varchar :display_name 255)

            (varchar :email 255 :unique)
            (check :email (> (length :email) 1))

            (varchar :password 255)

            (text :roles)
            (text :about)))
      (create (index :user [:username]))
      (create (index :user [:email])))
  
  (down [] (drop (table :user))))

(defmigration add-posts-table
  (up [] (create
          (tbl :post
               (varchar :title 200 :unique)
               (text :content)
               (boolean :hidden (default false))
               (refer-to :user))))
  (down [] (drop (table :post))))

(defmigration add-comments-table
  (up [] (create
          (tbl :comment
               (text :content)
               (boolean :hidden (default false))
               (refer-to :user)
               (refer-to :post))))
  (down [] (drop (table :comment))))

(defmigration add-sessions-table
  (up [] (create
          (tbl :user_session
               (varchar :key 255 :unique)
               (text :data)))
      (create (index :user_session [:key])))
  (down [] (drop (table :user_session))))

(defmigration add-favorites-table
  (up [] (create
          (tbl :favorite
               (refer-to :user)
               (refer-to :post)))
      (create (index :favorite [:user_id :post_id] :unique)))
  (down [] (drop (table :favorite))))