(ns gratefulplace.models.comment-notification
  (:refer-clojure :exclude [comment])
  (:require gratefulplace.models.db
            [gratefulplace.models.entities :as e])
  (:use korma.core
        gratefulplace.utils
        gratefulplace.models.entities))

(defn create!
  [attributes]
  (insert e/comment-notification (values attributes)))

(defn mark-all-viewed
  [user_id]
  (update e/comment-notification
          (set-fields {:viewed true})
          (where {:user_id (str->int user_id)})))

(defn all
  [user_id]
  (select e/comment-notification
          (with e/user (fields :username))
          (with e/comment (fields :content :created_on))
          (order :comment.created_on :DESC)
          (where {:user_id (str->int user_id)})))