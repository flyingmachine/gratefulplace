(ns gratefulplace.controllers.comments
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.comments :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only (if-valid view)]
        gratefulplace.controllers.common.content))

(defn create!
  [params]
  (if-valid
   params comment/validations errors
   (let [comment (comment/create! (assoc params
                                    :user_id
                                    (:id (friend/current-authentication))))]
     ;; TODO path stuff here
     (res/redirect (str "/posts/" (:post_id params) "#comment-" (:id comment))))
   (res/redirect (str "/posts/" (:post_id params) "?blank-comment=true"))))

(defn edit
  [params]
  (view
   view/edit
   :comment (comment/by-id (:id params))))

(def update (update-fn comment/by-id comment/update!))