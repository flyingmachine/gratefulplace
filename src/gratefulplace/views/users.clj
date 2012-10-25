(ns gratefulplace.views.users
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :only [*template-dir* defpage error-content]]))

(defpage show-new "users/new.html"
  [attributes errors]
  [:#username :input] (h/set-attr :value (:username attributes))
  [:#username :.errors] (error-content errors :username)
  
  [:#password :.errors] (error-content errors :password)
  
  [:#email :input] (h/set-attr :value (:email attributes))
  [:#email :.errors] (error-content errors :email))

(defpage show "users/show.html"
  [user]
  )