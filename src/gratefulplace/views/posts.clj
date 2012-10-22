(ns gratefulplace.views.posts
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :only [*template-dir* defpage content]]))

(defn comments
  [post]
  (let [comment-count (get :comment-count post 0)]
    (if (zero? comment-count)
      "Comment"
      (str  comment-count " comments"))))

(defn timestamp->string
  [timestamp]
  (-> (java.text.SimpleDateFormat. "MMM dd, yyyy hh:mma")
      (.format timestamp)))

(defn created-on
  [x]
  (timestamp->string (:created_on x)))

(defn post-path
  [post]
  (str "/posts/" (:id post)))

(defpage all "index.html"
  [posts]
  ;; don't show the second post as it's just an example
  [[:.post (h/nth-of-type 2)]] nil
  [:.post] (h/clone-for [post posts]
                        [:.author]   (h/content (:username post))
                        [:.date]     (h/content (created-on post))
                        [:.content]  (content post)
                        [:.comments] (h/do->
                                      (h/content (comments post))
                                      (h/set-attr :href (post-path post)))))

(defpage show "posts/show.html"
  [post]
  [:.post :.author]  (h/content (:username post))
  [:.post :.date]    (h/content (created-on post))
  [:.post :.content] (content post)
  [:#post_id] (h/set-attr :value (:id post))
  [:.comments :.comment] (h/clone-for [comment (:comment post)]
                                      [:.author]  (h/content (:username comment))
                                      [:.date]    (h/content (created-on comment))
                                      [:.content] (content comment)))

(defpage show-new "posts/new.html"
  [])