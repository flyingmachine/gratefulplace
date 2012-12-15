(ns gratefulplace.models.notification
  (:require [gratefulplace.models.comment-notification :as comment-notification])
  (:use gratefulplace.utils))

(defn notify 
  "initial coordinating function for creating notification records and sending email"
  [user comment]
  (comment-notification/create! {:user_id (:id user)
                                 :comment_id (:id comment)})
  (if (:receive_comment_notifications user)
    (mailer/send-new-comment user comment)))
