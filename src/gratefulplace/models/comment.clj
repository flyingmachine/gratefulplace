(ns gratefulplace.models.comment
  (:refer-clojure :exclude [comment])
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils
        gratefulplace.models.entities))

(def validations
  {:content
   ["Whoops! You forgot to write anything"
    #(> (count %) 1)]})

(defn create!
  [attributes]
  (insert e/comment (values attributes)))

(defn update!
  [conditions attributes]
  (let [attributes (dissoc attributes :id)]
    (update e/comment
            (set-fields attributes)
            (where (str->int conditions :id)))))

(defmacro all
  [& clauses]
  `(select e/comment
           (with e/user (fields :username))
           ~@clauses
           (order :created_on :DESC)))

(defn num-records
  ([] (num-records {}))
  
  ([conditions]
     ;; TODO fix this... shouldn't have to put in a bogus where
     (let [conditions (if (empty? conditions) true conditions)]
       ((comp :count first)
        (select e/comment
                (aggregate (count :*) :count)
                (where (str->int conditions :post_id :user_id)))))))

(defn by-id
  [id]
  (first (select e/comment
                 (where {:id (str->int id)}))))

