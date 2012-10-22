(ns gratefulplace.views.posts
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.controllers.common :only [*template-dir* nav]]))

(defn comments
  [post]
  (let [comment-count (get :comment-count post 0)]
    (if (zero? comment-count)
      "Add a comment"
      (str  comment-count " comments"))))

(defn timestamp->string
  [timestamp]
  (-> (java.text.SimpleDateFormat. "MMM dd, yyyy hh:mma")
      (.format timestamp)))

(h/deftemplate all (str *template-dir* "index.html")
  [posts]
  ;; don't show the second post as it's just an example
  [[:.post (h/nth-of-type 2)]] nil
  [:.post] (h/clone-for [post posts]
                                            [:.author]   (h/content (:username   post))
                                            [:.date]     (h/content (timestamp->string (:created_on post)))
                                            [:.content]  (h/content (:content    post))
                                            [:.comments] (h/content (comments    post)))
  [:nav] (h/substitute (nav false)))

(h/deftemplate new (str *template-dir* "posts/new.html")
  []
  [:nav] (h/substitute (nav false)))