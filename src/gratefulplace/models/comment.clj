(ns gratefulplace.models.comment
  (:refer-clojure :exclude [comment])
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils
        gratefulplace.models.helpers
        gratefulplace.models.entities))

(def validations
  {:content
   ["Whoops! You forgot to write anything"
    #(> (count %) 1)]})

(defn create!
  [attributes]
  (insert e/comment (values attributes)))

(def update! (updatefn e/comment))
(def by-id (by-idfn e/comment))
(def destroy! (destroyfn e/comment))

(defmacro all
  [& clauses]
  `(select e/comment
           (with e/user (fields :username))
           ~@clauses
           (order :created_on :DESC)))