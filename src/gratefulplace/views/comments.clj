(ns gratefulplace.views.comments
  (:require [net.cgrand.enlive-html :as h]
            markdown)
  (:use [gratefulplace.views.common :exclude [layout nav *template-dir*]]
        gratefulplace.utils
        [cemerick.friend :only (current-authentication)]))

(defpage edit "comments/edit.html"
  [comment]
  [:form]     (h/set-attr :action (comment-path comment))
  [:textarea] (h/content (:content comment)))