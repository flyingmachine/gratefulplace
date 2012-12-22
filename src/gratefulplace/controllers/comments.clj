(ns gratefulplace.controllers.comments
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.models.user :as user]
            [gratefulplace.models.notification :as notification]
            [gratefulplace.models.post :as post]
            [gratefulplace.views.comments :as view])

  (:use gratefulplace.controllers.common
        [flyingmachine.webutils.validation :only (if-valid)]
        gratefulplace.controllers.common.content
        gratefulplace.paths
        gratefulplace.models.permissions))

(defn create!
  [params]
  (if-valid
   params comment/validations errors
   (let [comment (comment/create! (assoc params
                                    :user_id
                                    (current-user-id)))
         post-owner-id (:user_id (post/by-id (:post_id params)))]
     
     (if (not-current-user-id? post-owner-id)
       (future (notification/notify
                (user/one {:id post-owner-id})
                comment)))
     (res/redirect (post-path (:post_id params) "#comment-" (:id comment))))
   (res/redirect (post-path (:post_id params) "?blank-comment=true"))))

(defn edit
  [params]
  (view
   view/edit
   :comment (comment/by-id (:id params))))

(def update (update-fn comment/by-id comment/update!))

(defn destroy
  [params]
  ((destroy-fn comment/by-id comment/destroy!) params)
  (res/redirect (post-path (:post_id params))))