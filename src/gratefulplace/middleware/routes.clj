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
  (POST "/posts"     [content] (posts/create! {:content content}))
  (GET  "/users/new" [] (users/new))
  (POST "/users" [username email] (users/create! {:username username :email email}))
  (compojure.route/not-found "Sorry, there's nothing here."))