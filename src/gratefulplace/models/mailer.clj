(ns gratefulplace.models.notification
  (:require [gratefulplace.models.comment-notification :as comment-notification])
  (:use gratefulplace.utils))


(defn send-new-comment 
  "send an email for a new comment"
  [user comment]
  )