(ns gratefulplace.controllers.comments
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.comment :as comment]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only [*template-dir* nav]]))

(defn create!
  [params]
  (comment/create! (assoc params
                     :user_id
                     (:id (friend/current-authentication))))
  (res/redirect (str "/posts/" (:post_id params))))