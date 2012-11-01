(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.posts     :as posts]
            [gratefulplace.controllers.users     :as users]
            [gratefulplace.controllers.comments  :as comments]
            [gratefulplace.controllers.favorites :as favorites]
            [gratefulplace.controllers.session   :as session]
            [gratefulplace.models.user :as user]
            [cemerick.friend :as friend])
  (:use [compojure.core :only (GET PUT POST ANY defroutes)]))


(defmacro route
  [method path handler]
  `(~method ~path req#
            (~handler req#)))

(defroutes routes
  (compojure.route/files "/" {:root "public"})

  ;; posts
  (route GET "/" posts/all)
  (route GET "/posts" posts/all)
  (route GET "/posts/new" posts/show-new)
  (POST "/posts" {params :params}
        (friend/authorize #{:user} (posts/create! params)))
  (route GET "/posts/:id" posts/show)
  (route GET "/posts/:id/edit" posts/edit)
  (route POST "/posts/:id" posts/update)
  
  ;; comments
  (POST "/comments" {params :params}
        (friend/authorize #{:user} (comments/create! params)))

  (route GET "/comments/:id/edit" comments/edit)
  (route POST "/comments/:id" comments/update)

  ;; users
  (route GET "/users/new" users/show-new)
  (route POST "/users" users/create!)
  (route GET "/users/:username" users/show)
  (route GET "/users/:username/edit" users/edit)
  (route GET "/users/:username/posts" users/posts)
  (route GET "/users/:username/comments" users/comments)
  (route POST "/users/:username" users/update)
  
  ;; favorites
  (route GET "/favorites" favorites/all)
  (POST "/favorites/:post_id" [post_id]
        (friend/authorize #{:user} (favorites/create! post_id)))
  (POST "/favorites/:post_id/destroy" [post_id]
        (friend/authorize #{:user} (favorites/destroy! post_id)))

  ;; auth
  (route GET "/login" session/show-new)
  (route POST "/login" session/show-new)
  (friend/logout
   (ANY "/logout" []
        (ring.util.response/redirect "/")))
  
  (compojure.route/not-found "Sorry, there's nothing here."))