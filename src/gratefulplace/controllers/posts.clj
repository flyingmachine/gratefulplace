(ns gratefulplace.controllers.posts
  (:require [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [ring.util.response :as res]
            [gratefulplace.controllers.common :as common]))

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

(h/deftemplate all (str common/*template-dir* "index.html")
  []
  ;; don't show the second post as it's just an example
  [[:.post (h/nth-of-type 2)]] nil
  [:.post] (h/clone-for [post (post/all)]
                                            [:.author]   (h/content (:username   post))
                                            [:.date]     (h/content (timestamp->string (:created_on post)))
                                            [:.content]  (h/content (:content    post))
                                            [:.comments] (h/content (comments    post)))
  [:nav] (h/substitute (common/nav false)))

(h/deftemplate new (str common/*template-dir* "posts/new.html")
  []
  [:nav] (h/substitute (common/nav false)))

(defn create!
  [attributes]
  (post/create! attributes)
  (res/redirect "/"))