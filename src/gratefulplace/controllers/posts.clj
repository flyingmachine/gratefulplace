(ns gratefulplace.controllers.posts
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.posts :as view]
            [cemerick.friend :as friend]
            korma.core)

  (:use [gratefulplace.controllers.common :only (if-valid)]
        gratefulplace.controllers.common.content
        gratefulplace.utils
        gratefulplace.models.permissions))

;; TODO any way I could tidy this up?
(defn all
  []
  (let [current-auth (friend/current-authentication)]
    (view/all 
     (cond
      (moderator? (:username current-auth))
      (post/all)
      
      current-auth
      (post/all
       (korma.core/where
        (or {:hidden false}
            {:user_id [= (:id current-auth)]})))
      
      :else
      (post/all
       (korma.core/where {:hidden false})))
     current-auth)))

(defn show
  [id]
  (let [current-auth (friend/current-authentication)
        base-cond {:post_id (str->int id)}
        comments (cond
                  (moderator? (:username current-auth))
                  (comment/all
                   (korma.core/where base-cond))
                  
                  current-auth
                  (comment/all
                   (korma.core/where
                    (and
                     base-cond
                     (or {:hidden false}
                         {:user_id [= (:id current-auth)]}))))
                  
                  :else
                  (comment/all (korma.core/where (and base-cond {:hidden false}))))]
    
    (view/show (post/by-id id)
               comments
               (friend/current-authentication))))

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
   params post/validations errors
   (do
     (post/create! (assoc params
                     :user_id
                     (:id (friend/current-authentication))))
     (res/redirect "/"))
   (view/show-new params errors)))