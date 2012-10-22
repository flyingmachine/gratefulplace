(ns gratefulplace.controllers.posts
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.posts :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only [*template-dir* nav]]))

(defn all
  []
  (view/all (post/all)))

(defn show
  [id]
  (view/show (post/by-id id) (comment/all {:post_id id})))

(defn show-new
  []
  (view/show-new))

(defn create!
  [params]
  (post/create! (assoc params
                  :user_id
                  (:id (friend/current-authentication))))
  (res/redirect "/"))