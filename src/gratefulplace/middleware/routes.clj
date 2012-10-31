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

(defroutes routes
  (compojure.route/files "/" {:root "public"})

  ;; posts
  (GET  "/" []
        (posts/all))
  
  (GET  "/posts" []
        (posts/all))
  
  (GET  "/posts/new" []
        (posts/show-new))
  
  (POST "/posts" {params :params}
        (friend/authorize #{:user} (posts/create! params)))
  
  (GET  "/posts/:id" [id]
        (posts/show id))
  
  (GET  "/posts/:id/edit" [id]
        (posts/edit id))

  (POST "/posts/:id" {params :params}
        (posts/update params))
  
  ;; comments
  (POST "/comments" {params :params}
        (friend/authorize #{:user} (comments/create! params)))

  (GET  "/comments/:id/edit" [id]
        (comments/edit id))

  (POST "/comments/:id" {params :params}
        (comments/update params))

  ;; users
  (GET  "/users/new" []
        (users/show-new))
  
  (POST "/users" {params :params}
        (users/create! params))

  (GET  "/users/:username" [username]
        (users/show username))
  
  (GET  "/users/:username/edit" [username]
        (users/edit username))

  (GET  "/users/:username/posts" [username]
        (users/posts username))

  (GET  "/users/:username/comments" [username]
        (users/comments username))

  (POST "/users/:username" {params :params}
        (users/update params))

  ;; favorites
  (GET  "/favorites" []
        (favorites/all))
  (POST "/favorites/:post_id" [post_id]
        (favorites/create! post_id))
  
  (POST "/favorites/:post_id/destroy" [post_id]
        (favorites/destroy! post_id))

  ;; auth
  (GET "/login" {params :params}
       (session/show-new params))
  (POST "/login" []
       (session/show-new))
  (friend/logout
   (ANY "/logout" request
        (ring.util.response/redirect "/")))
  
  (compojure.route/not-found "Sorry, there's nothing here."))