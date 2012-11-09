(ns gratefulplace.models.helpers
  (:use korma.core))

(defmacro paginate
  ([page num-per-page query]
     `(-> ~@query
       (limit ~num-per-page)
       (offset (dec ~page)))))