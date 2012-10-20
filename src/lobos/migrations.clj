(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema
               config helpers)))

(defmigration add-users-table
  (up [] (create
          (tbl :user
            (varchar :username 50 :unique)
            (check :username (> (length :username) 1))

            (varchar :display_name 255)

            (varchar :email 255 :unique)
            (check :email (> (length :email) 1))

            (varchar :password 255)

            (text :roles))

          (index :user [:username :email])))
  
  (down [] (drop (table :user))))

(defmigration add-posts-table
  (up [] (create
          (tbl :post
            (varchar :title 200 :unique)
            (text :content)
            (refer-to :user))))
  (down [] (drop (table :post))))

(defmigration add-comments-table
  (up [] (create
          (tbl :comment
            (text :content)
            (refer-to :user)
            (refer-to :post))))
  (down [] (drop (table :comment))))