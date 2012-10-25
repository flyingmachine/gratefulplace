(ns gratefulplace.views.users
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common
         :only [*template-dir* defpage error-content content relation-count]]))

(defpage show-new "users/new.html"
  [attributes errors]
  [:#username :input] (h/set-attr :value (:username attributes))
  [:#username :.errors] (error-content errors :username)
  
  [:#password :.errors] (error-content errors :password)
  
  [:#email :input] (h/set-attr :value (:email attributes))
  [:#email :.errors] (error-content errors :email))

(defpage show "users/show.html"
  [user]
  [:title]         (h/content (str "About " (:username user) " :: Grateful Place"))
  [:h2 :.username] (h/content (:username user))
  [:.about]        (content (:about user))

  [:.links :.posts :.count]    (h/content (str (relation-count user :post)))
  [:.links :.comments :.count] (h/content (str (relation-count user :comment))))