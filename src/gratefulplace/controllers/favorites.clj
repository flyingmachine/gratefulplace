(ns gratefulplace.controllers.favorites
  (require [gratefulplace.models.favorite :as favorite])
  (use gratefulplace.models.permissions))

(defn create!
  [post_id]
  (favorite/create! post_id (current-user-id))
  {:status 200})

(defn destroy!
  [post_id]
  (favorite/destroy! post_id (current-user-id))
  {:status 200})

(defn all
  [])