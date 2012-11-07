(ns gratefulplace.views.common-test
  (:use clojure.test
        gratefulplace.views.common
        gratefulplace.views.test-helpers))

(def node
  {:tag :div
   :content ""
   :attrs {}})

(deftest nav-test
  (testing "Shows log out when logged in is true"
    (is (snippet-contains? "Log Out" (nav true))))

  (testing "Shows log out when logged in is true"
    (is (snippet-contains? "Log In" (nav false)))))

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

;; TODO could make this less complex
(deftest set-path-test
  (testing "Sets the :href attribute of a node"
    (is (attr-set? ((set-path "chester" user-path) node)
                    :href "/users/chester"))))

(deftest md-content-test
  (testing "Sets node content to markdownified string"
    (is (content-set? ((md-content "# h1 test") node)
                       "<div><h1> h1 test</h1></div>"))))

(deftest format-error-messages-test
  (testing "Creates UL correctly"
    (is (= (format-error-messages ["error one" "error two"])
           "<ul><li>error one</li><li>error two</li></ul>"))))

(deftest error-content-test
  (testing "Sets error message for content if message exists for key"
    (is (content-set? ((error-content {:password ["Must enter"]} :password) node)
                      "<div><ul><li>Must enter</li></ul></div>")))
  (testing "Returns nil when requesting message for a key with no errors"
    (is (= (error-content {:password ["Must enter"]} :username)
           nil))))

(deftest relation-count-test
  (testing "Returns relation count"
    (is (= (relation-count {:posts [{:count 10}]} :posts)
           10))))

(deftest linked-username-test
  (testing "Sets content of link to username and href to user path"
    (is (content-set? ((linked-username {:username "jack"}) {:tag :a})
                      "<a href=\"/users/jack\">jack</a>"))))

(deftest keep-when-test
  (testing "Returns nil when the condition is false"
    (is (nil? (keep-when false))))

  (testing "Returns identity function when the condition is true"
    (is (= ((keep-when true) 10)
           10))))


