(ns gratefulplace.models.helpers-test
  (:use clojure.test
        gratefulplace.models.helpers))

(deftest paginate-test
  (testing "Generate correct macro expansion"
    (is (= (macroexpand-1 '(paginate 1 10
                                     (gratefulplace.models.post/all
                                      (where {:hidden false}))))
           '(gratefulplace.models.post/all
             (where {:hidden false})
             (korma.core/limit 10)
             (korma.core/offset (clojure.core/dec 1)))))))