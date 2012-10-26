(ns gratefulplace.models.user-session
  (:refer-clojure :exclude [comment])
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e]
            crypto.random)
  (:use korma.core
        gratefulplace.utils
        gratefulplace.models.entities))

(defn one
  [conditions]
  (first (select e/user_session
                 (where conditions)
                 (limit 1))))

(defn create!
  [attributes]
  (let [key (crypto.random/hex 20)]
    (insert e/user_session
            (values (assoc attributes :key key)))))

(defn update!
  [key attributes]
  (update e/user_session
          (set-fields attributes)
          (where {:key key})))

(defn destroy!
  [key]
  (delete e/user_session
          (where {:key key})))