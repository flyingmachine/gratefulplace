(ns gratefulplace.controllers.posts
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.posts :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only (if-valid view with-visibility)]
        gratefulplace.controllers.common.content
        gratefulplace.utils
        gratefulplace.models.permissions
        gratefulplace.models.helpers
        [clojure.contrib.math :only (ceil)]
        [korma.core :only (where)]))

;; TODO any way I could tidy this up?
(defn all
  [req]
  (let [current-auth (friend/current-authentication)
        per-page 20
        page (str->int (or (get-in req [:params :page] 1)))
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
  [req]
  (let [id (get-in req [:params :id])
        current-auth (friend/current-authentication)
        base-cond {:post_id (str->int id)}]
    
    (view
     view/show
     :post (post/by-id id)
     :comments (with-visibility
                 current-auth
                 {:moderator (comment/all
                              (where base-cond))
                  
                  :logged-in (comment/all
                              (where
                               (and
                                base-cond
                                (or {:hidden false}
                                    {:user_id [= (:id current-auth)]}))))
                  
                  :not-logged-in (comment/all (where (and base-cond {:hidden false})))}))))

(defn edit
  [req]
  (view
   view/edit
   :post (post/by-id (get-in req [:params :id]))))

(def update (update-fn post/by-id post/update!))

(defn show-new
  [req]
  (view view/show-new))

(defn create!
  [req]
  (let [params (:params req)]
    (if-valid
     params post/validations errors
     (do
       (post/create! (assoc params
                       :user_id
                       (:id (friend/current-authentication))))
       (res/redirect "/"))
     (view view/show-new :errors errors))))