(ns gratefulplace.views.posts
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common
         :only [*template-dir* defpage md-content error-content user-path]]
        [cemerick.friend :only (current-authentication)]))

(defn comments
  [post]
  (let [comment-count (get-in post [:comment 0 :count] 0)]
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


(defn username
  [record]
  (h/do->
   (h/content (:username record))
   (h/set-attr :href (user-path record))))

;; TODO refactor all this username access
(defpage all "index.html"
  [posts]
  ;; don't show the second post as it's just an example
  [[:.post (h/nth-of-type 2)]] nil
  [:.post] (h/clone-for [post posts]
                        [:.author]   (username post)
                        [:.date]     (h/content (created-on post))
                        [:.content]  (md-content post)
                        [:.comments] (h/do->
                                      (h/content (comments post))
                                      (h/set-attr :href (post-path post)))))

(defpage show "posts/show.html"
  [post]
  [:.post :.author]      (username post)
  [:.post :.date]        (h/content (created-on post))
  [:.post :.content]     (md-content post)
  [:#post_id]            (h/set-attr :value (:id post))
  [:.comments :.comment] (h/clone-for [comment (:comment post)]
                                      [:.author]  (username comment)
                                      [:.date]    (h/content (created-on comment))
                                      [:.content] (md-content comment)))

(defpage show-new "posts/new.html"
  [attributes errors]
  [:#content :textarea] (h/content (:content attributes))
  [:#content :.errors] (if (current-authentication)
                         (error-content errors :content)
                         (h/content "You'll need to log in to post")))