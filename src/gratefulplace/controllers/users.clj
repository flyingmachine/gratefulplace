(ns gratefulplace.controllers.users
  (:require [net.cgrand.enlive-html :as h]
            [gratefulplace.models.user :as user]
            [ring.util.response :as res]))

(h/deftemplate new "gratefulplace/templates/users/new.html"
  [])