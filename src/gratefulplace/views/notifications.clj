(ns gratefulplace.views.notifications
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :exclude [layout nav *template-dir*]]
        gratefulplace.utils
        gratefulplace.models.permissions))

;; TODO handle case where there are no notifications
(defpage all "notifications/index.html"
  [notifications]
  [:title] (h/content (str (current-username) "'s Notifications :: Grateful Place"))
  [[:div.comment (h/nth-of-type 2)]] nil
  [:div.comment] (h/clone-for [notif notifications]
                              h/this-node   (if (not (:viewed notif))
                                              (h/add-class "highlight"))
                              [:.date]      (h/content (created-on notif))
                              [:.content]   (h/content (:content notif))
                              [:.author :a] (linked-username notif)
                              [:.more :a]   (set-path {:id (:comment_id notif)} comment-path)))