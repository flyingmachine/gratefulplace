(ns gratefulplace.models.helpers
  (:use korma.core
        gratefulplace.utils))

;; TODO make better!
(defmacro paginate
  ([page num-per-page query]
     `(~@query
       (limit ~num-per-page)
       (offset (* ~num-per-page (dec ~page))))))

(defn destroyfn
  [entity]
  (fn [conditions]
    (delete entity
            (where (str->int conditions :id)))))

(defn by-idfn
  [entity]
  (fn [id]
    (first (select entity
                   (where {:id (str->int id)})))))

(defn updatefn
  [entity]
  (fn [conditions attributes]
    (let [attributes (dissoc attributes :id)]
      (update entity
              (set-fields attributes)
              (where (str->int conditions :id))))))