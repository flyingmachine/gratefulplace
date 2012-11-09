(ns gratefulplace.models.helpers
  (:use korma.core))

;; TODO make better!
(defmacro paginate
  ([page num-per-page query]
     `(~@(macroexpand-1 query)
       (limit ~num-per-page)
       (offset (dec ~page)))))