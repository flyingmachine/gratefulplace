(ns gratefulplace.controllers.comments
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.comment :as comment]
            [cemerick.friend :as friend]))

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