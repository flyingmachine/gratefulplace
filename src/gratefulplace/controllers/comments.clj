(ns gratefulplace.controllers.comments
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.comments :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only (if-valid)]
        gratefulplace.models.permissions))

(def validations
  [[:content
    ["Whoops! You forgot to write anything"
     #(> (count %) 4)]]])

(defn create!
  [params]
  (comment/create! (assoc params
                     :user_id
                     (:id (friend/current-authentication))))
  (res/redirect (str "/posts/" (:post_id params))))

(defn edit
  [id]
  (println (comment/by-id id))
  (view/edit (comment/by-id id)))

(defn update
  [params]
  (let [id       (:id params)
        comment  (comment/by-id id)]
    (protect
     (modify-content? (:user_id comment))
     (comment/update! {:id id} params)
     (view/updated params))))