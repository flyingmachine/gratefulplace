(ns gratefulplace.controllers.posts
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.posts :as view]
            [cemerick.friend :as friend]))

(def validations
  [[:content
    ["Whoops! You forgot to write anything"
     #(> (count %) 4)]]])

(defn all
  []
  (view/all (post/all)))

(defn show
  [id]
  (view/show (post/by-id id)))

(defn show-new
  []
  (println (friend/current-authentication))
  (view/show-new))

(defn create!
  [params]
  (post/create! (assoc params
                  :user_id
                  (:id (friend/current-authentication))))
  (res/redirect "/"))