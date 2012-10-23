(ns gratefulplace.models.user
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e]
            (cemerick.friend [credentials :as creds]))
  (:use korma.core))

(defn- create-input->db-fields [input]
  (merge input
         {:password (creds/hash-bcrypt (:password input))
          :roles (str ["user"])}))

(defn create!
  [attributes]
  (insert e/user (values (create-input->db-fields attributes))))

(defn one
  [conditions]
  (first (select e/user
                 (where conditions)
                 (limit 1))))