(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.posts    :as posts]
            [gratefulplace.controllers.users    :as users]
            [gratefulplace.controllers.comments :as comments]
            [gratefulplace.controllers.session  :as session]
            [gratefulplace.models.user :as user])
  (:use [compojure.core :only (GET PUT POST defroutes)]))

(defroutes routes
  (compojure.route/files "/" {:root "public"})

  ;; posts
  (GET  "/" [] (posts/all))
  (GET  "/posts" [] (posts/all))
  (GET  "/posts/new" [] (posts/show-new))
  (POST "/posts"     {params :params} (posts/create! params))
  (GET  "/posts/:id" [id] (posts/show id))

  ;; comments
  (POST "/comments"  {params :params} (comments/create! params))

  ;; users
  (GET  "/users/new" [] (users/show-new))
  (POST "/users"     {params :params} (users/create! params))

  ;; auth
  (GET "/session/new" [] (session/show-new))
  
  (compojure.route/not-found "Sorry, there's nothing here."))