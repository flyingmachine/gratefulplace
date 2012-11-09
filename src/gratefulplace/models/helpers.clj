(ns gratefulplace.models.helpers
  (:use korma.core))

(defmacro paginate
  [page num-per-page query]
  (let [[fname & rest] query]
    (conj rest
          (list 'korma.core/limit num-per-page)
          (list 'korma.core/offset (* num-per-page (dec page)))
          fname)))