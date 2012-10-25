(ns gratefulplace.models.comment
  (:refer-clojure :exclude [comment])
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils
        gratefulplace.models.entities))

(defn create!
  [attributes]
  (insert e/comment (values attributes)))


(defn optional-conditions
  [sel conditions where]
  (if (empty? conditions)
    sel
    (-> sel where)))

(defn all
  ([]
     (all {}))
  ([conditions]
     (select e/comment
             (with e/user
                   (fields :username))
             (where (str->int conditions :post_id :user_id))
             (order :created_on :DESC))))

(defn num-records
  ([] (num-records {}))
  
  ([conditions]
     ;; TODO fix this... shouldn't have to put in a bogus where
     (let [conditions (if (empty? conditions) true conditions)]
       ((comp :count first)
        (select e/comment
                (aggregate (count :*) :count)
                (where (str->int conditions :post_id :user_id)))))))