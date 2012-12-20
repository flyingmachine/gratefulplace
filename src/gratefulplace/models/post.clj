(ns gratefulplace.models.post
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils
        gratefulplace.models.helpers))

(def validations
  {:content
   ["Whoops! You forgot to write anything"
    #(> (count %) 1)]})

(defn create!
  [attributes]
  (insert e/post (values attributes)))

(def update! (updatefn e/post))
(def destroy! (destroyfn e/post))

(def base
  (->
   (select* e/post)
   (with e/user
         (fields :username))
   (with e/comment
         (aggregate (count :*) :count)
         (where {:hidden false}))
   (with e/favorite
         (aggregate (count :*) :count))
   (order :created_on :DESC)))

(defmacro all
  [& clauses]
  `(-> base
       ~@clauses
       select))

(defmacro record-count
  [& clauses]
  `(:cnt
    (first
     (select e/post
             (aggregate (~'count :*) :cnt)
             ~@clauses))))

(defn by-id
  [id]
  (first
   (-> base
       (where {:id (str->int id)})
       select)))
