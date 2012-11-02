(ns gratefulplace.views.favorites
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :exclude [layout nav *template-dir*]]
        gratefulplace.utils
        gratefulplace.models.permissions))

;; TODO handle case where there are no posts
(defpage all "favorites/index.html"
  [posts]
  [:title] (h/content (str (current-username) "'s Favorites :: Grateful Place"))
  [[:div.post         (h/nth-of-type 2)]] nil
  [:div.post]         (h/clone-for [post posts]
                                   [:.date]    (h/content (created-on post))
                                   [:.content] (h/content (:content post))
                                   [:a]        (set-path post post-path)))