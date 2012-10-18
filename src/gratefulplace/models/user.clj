(ns gratefulplace.models.user
  (:require 
            (cemerick.friend [credentials :as creds]))
  (:use korma.core))

(defentity users)

(defn- create-input->db-fields [input]
  (merge input
         {:password (creds/hash-bcrypt (:password input))
          :roles ["user"]}))

(defn create!
  [attributes]
  (insert users (values (create-input->db-fields attributes))))

(defn one
  [find])