(ns gratefulplace.controllers.posts
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.posts :as view]
            [cemerick.friend :as friend])

  (:use gratefulplace.controllers.common
        [flyingmachine.webutils.validation :only (if-valid)]
        gratefulplace.controllers.common.content
        gratefulplace.utils
        gratefulplace.paths
        gratefulplace.models.permissions
        gratefulplace.models.helpers
        gratefulplace.lib.twitter
        [clojure.contrib.math :only (ceil)]
        [korma.core :only (where)]))

;; TODO any way I could tidy this up?
(defn all
  [params]
  (let [current-auth (friend/current-authentication)
        per-page 20
        page (str->int (or (get params :page 1)))
        conditions (with-visibility
                     current-auth
                     {:moderator true
                      :logged-in (or {:hidden false}
                                     {:user_id [= (:id current-auth)]})
                      :not-logged-in {:hidden false}})
        record-count (post/record-count (where conditions))]
    (view
     view/all
     :posts (paginate page per-page (post/all (where conditions)))
     :record-count record-count
     :page page
     :max-pages (ceil (/ record-count per-page)))))

(defn show
  [params]
  (let [id (:id params)
        current-auth (friend/current-authentication)
        comment-base-cond {:post_id (str->int id)}
        comments (with-visibility
                   current-auth
                   {:moderator (comment/all (where comment-base-cond))
                    :logged-in (comment/all (where (and comment-base-cond
                                                                  (or {:hidden false}
                                                                      {:user_id [= (:id current-auth)]}))))
                    :not-logged-in (comment/all (where (merge comment-base-cond {:hidden false})))})]
    (view
     view/show
     :post (post/by-id id)
     :comments comments)))

(defn edit
  [params]
  (view
   view/edit
   :post (post/by-id (:id params))))

(def update (update-fn post/by-id post/update!))

(defn destroy
  [params]
  ((destroy-fn post/by-id post/destroy!) params)
  (res/redirect "/"))

(defn show-new
  [params]
  (view view/show-new))

(defn create!
  [params]
  (if-valid
   params post/validations errors
   (do
     (let [post (post/create! (assoc params
                                :user_id
                                (:id (friend/current-authentication))))]
       (future (send-tweets! (:content params) (post-path post))))
     (res/redirect "/"))
   (view view/show-new :errors errors)))