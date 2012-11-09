(ns gratefulplace.controllers.posts
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.post :as post]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.posts :as view]
            [cemerick.friend :as friend]
            korma.core)

  (:use [gratefulplace.controllers.common :only (if-valid view)]
        gratefulplace.controllers.common.content
        gratefulplace.utils
        gratefulplace.models.permissions
        gratefulplace.models.helpers))

;; TODO any way I could tidy this up?
(defn all
  [req]
  (let [current-auth (friend/current-authentication)
        page (str->int (or (get-in req [:params :page] 1)))]
    (view
     view/all
     :posts (cond
             (moderator? (:username current-auth))
             (paginate page 20 (post/all))
             
             current-auth
             (paginate page 20
                       (post/all
                        (korma.core/where
                         (or {:hidden false}
                             {:user_id [= (:id current-auth)]}))))
             
             :else
             (paginate page 20
                       (post/all
                        (korma.core/where {:hidden false})))))))

(defn show
  [req]
  (let [id (get-in req [:params :id])
        current-auth (friend/current-authentication)
        base-cond {:post_id (str->int id)}]
    
    (view
     view/show
     :post (post/by-id id)
     :comments (cond
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
                  (comment/all (korma.core/where (and base-cond {:hidden false})))))))

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