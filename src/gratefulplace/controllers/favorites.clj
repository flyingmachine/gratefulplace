(ns gratefulplace.controllers.favorites
  (require [gratefulplace.models.favorite :as favorite]
           [gratefulplace.views.favorites :as view])
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
  []
  (view/all
   (if (current-user-id)
     (favorite/all (current-user-id))
     [])))