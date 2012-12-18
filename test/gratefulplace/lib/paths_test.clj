(ns gratefulplace.lib.paths-test
  (:use clojure.test
        gratefulplace.lib.paths))

(deftest path-test
  (testing "Uses url key to generate url"
    (is (= (path {:username "chestnut"} :username "users")
           "/users/chestnut")))

  (testing "Uses record itself when url key doesn't apply"
    (is (= (path "chestnut" :username "users")
           "/users/chestnut")))
  
  (testing "Appends suffixes"
    (is (= (path "chestnut" :username "users" "posts")
           "/users/chestnut/posts"))
    (is (= (path "chestnut" :username "users" "posts" "all")
           "/users/chestnut/posts/all"))))

(deftest create-path-fns-test
  (testing "generates functions that work"
    (create-path-fns "post" :id "edit" "destroy")
    (is (= (post-edit-path {:id 1})
           "/posts/1/edit"))
    (is (= (post-edit-path 1)
           "/posts/1/edit"))
    (is (= (post-destroy-path {:id 1})
           "/posts/1/destroy"))
    (is (= (post-destroy-path 1)
           "/posts/1/destroy"))
    (is (= (post-path 1 "#comments")
           "/posts/1#comments"))
    (is (= (post-path 1 "#" "comments")
           "/posts/1#comments"))))