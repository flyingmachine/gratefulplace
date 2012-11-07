(ns gratefulplace.utils-test
  (:use clojure.test
        gratefulplace.utils))

(deftest str->int-test
  (testing "Convert a string to an integer"
    (is (= (str->int "3")
           3)))

  (testing "Specified keys should be converted to integers"
    (is (= (str->int {:a "3" :b "5"} :a)
           {:a 3 :b "5"}))))

(deftest self-unless-fn-test
  (testing "Returns 'then' argument when function applied to 'self' returns true"
    (is (= (self-unless-fn 1 identity 0)
           0)))

  (testing "Returns 'self' argument when function applied to 'self' returns false"
    (is (= (self-unless-fn 1 #(not %) 0)
           1))))