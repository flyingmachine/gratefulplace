(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.posts    :as posts]
            [gratefulplace.controllers.users    :as users]
            [gratefulplace.controllers.comments :as comments]
            [gratefulplace.controllers.session  :as session]
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

  ;; comments
  (POST "/comments" {params :params}
        (friend/authorize #{:user} (comments/create! params)))

  ;; users
  (GET  "/users/new" []
        (users/show-new))
  
  (POST "/users" {params :params}
        (users/create! params))

  (GET  "/users/:username" [username]
        (users/show username))

  ;; auth
  (GET "/login" []
       (session/show-new))
  (POST "/login" []
       (session/show-new))
  (friend/logout
   (ANY "/logout" request
        (ring.util.response/redirect "/")))
  
  (compojure.route/not-found "Sorry, there's nothing here."))