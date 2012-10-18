(ns gratefulplace.controllers.posts
  (:require [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [ring.util.response :as res]
            [gratefulplace.controllers.common :as common]))

(def posts
  [{:author        "Terrence Blowfish"
    :date          "Jan 03, 2012 9:53am"
    :content       "Today I'm grateful for the trees turning colors"
    :comment-count 10}
   {:author        "Mickey Parkplace"
    :date          "Jan 03, 2012 10:12am"
    :content       "This morning I feel grateful for the food in my kitchen. I'm happy for my functioning oil heater and for the existence of matcha tea."
    :comment-count 3}
   {:author        "Jeremy McWilder"
    :date          "Jan 03, 2012 10:14am"
    :content       "I'm grateful for the cat in my lap"
    :comment-count 0}])

(defn comments [post]
  (if (zero? (:comment-count post))
    "Add a comment"
    (str (:comment-count post) " comments")))

(h/deftemplate all (str common/*template-dir* "index.html")
  []
  [[:.post (h/nth-of-type 1)]] (h/clone-for [post posts]
                        [:.author]   (h/content (:author post))
                        [:.date]     (h/content (:date post))
                        [:.content]  (h/content (:content post))
                        [:.comments] (h/content (comments post))
                        (h/content "This is enlive content"))
  [[:.post (h/nth-of-type 2)]] nil
  [:nav] (h/substitute (common/nav false)))

(h/deftemplate new (str common/*template-dir* "posts/new.html")
  []
  [:nav] (h/substitute (common/nav false)))

(defn create!
  [attributes]
  (post/create! attributes)
  (res/redirect "/"))