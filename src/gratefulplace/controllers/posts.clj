(ns gratefulplace.controllers.posts
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.posts :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only (if-valid)]
        gratefulplace.controllers.common.content))

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

(defn edit
  [id]
  (view/edit (post/by-id id)))

(def update (update-fn post/by-id post/update!))

(defn show-new
  []
  (view/show-new nil nil))

(defn create!
  [params]

  (if-valid
   params validations errors
   (do
     (post/create! (assoc params
                     :user_id
                     (:id (friend/current-authentication))))
     (res/redirect "/"))
   (view/show-new params errors)))