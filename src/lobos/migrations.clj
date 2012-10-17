(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema
               config helpers)))

(defmigration add-users-table
  (up [] (create
          (tbl :users
            (varchar :username 50 :unique)
            (check :username (> (length :username) 1))

            (varchar :display_name 255)

            (varchar :email 255 :unique)
            (check :email (> (length :email) 1))

            (varchar :password 255))

          (index :users [:username :email])))
  
  (down [] (drop (table :users))))

(defmigration add-posts-table
  (up [] (create
          (tbl :posts
            (varchar :title 200 :unique)
            (text :content)
            (refer-to :users))))
  (down [] (drop (table :posts))))

(defmigration add-comments-table
  (up [] (create
          (tbl :comments
            (text :content)
            (refer-to :users)
            (refer-to :posts))))
  (down [] (drop (table :comments))))