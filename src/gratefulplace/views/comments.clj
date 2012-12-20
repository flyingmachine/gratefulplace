(ns gratefulplace.views.comments
  (:require [net.cgrand.enlive-html :as h]
            markdown)
  (:use [gratefulplace.views.common :exclude [layout nav *template-dir*]]
        gratefulplace.utils
        gratefulplace.paths
        [cemerick.friend :only (current-authentication)]))

(defpage edit "comments/edit.html"
  [comment]
  [:form.update] (h/set-attr :action (comment-path comment))
  [:form.delete] (h/set-attr :action (comment-destroy-path comment))
  [:textarea] (h/content (:content comment))
  [:.post_id] (h/set-attr :value (:post_id comment)))