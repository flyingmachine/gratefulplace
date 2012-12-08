(ns gratefulplace.models.helpers
  (:use korma.core))

;; TODO make better!
(defmacro paginate
  ([page num-per-page query]
     `(~@query
       (limit ~num-per-page)
       (offset (* num-per-page (dec ~page))))))