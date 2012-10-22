(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.posts :as posts]
            [gratefulplace.controllers.users :as users]
            [gratefulplace.models.user :as user])
  (:use [compojure.core :only (GET PUT POST defroutes)]))

(defroutes routes
  (compojure.route/files "/" {:root "public"})
  (GET  "/" [] (posts/all))
  (GET  "/posts/new" [] (posts/new))
  (POST "/posts"     {params :params} (posts/create! params))
  
  (GET  "/users/new" [] (users/new))
  (POST "/users"     {params :params} (users/create! params))
  
  (compojure.route/not-found "Sorry, there's nothing here."))