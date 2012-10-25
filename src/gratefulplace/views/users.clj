(ns gratefulplace.views.users
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common
         :only [*template-dir* defpage error-content md-content relation-count]]
        gratefulplace.utils))

(defpage show-new "users/new.html"
  [attributes errors]
  [:#username :input] (h/set-attr :value (:username attributes))
  [:#username :.errors] (error-content errors :username)
  
  [:#password :.errors] (error-content errors :password)
  
  [:#email :input] (h/set-attr :value (:email attributes))
  [:#email :.errors] (error-content errors :email))

(defn about-content
  [user]
  (md-content
   (let [about (:about user)]
     (self-unless-fn about empty?
       "This user hasn't entered info yet."))))

(defpage show "users/show.html"
  [user]
  [:title]         (h/content (str "About " (:username user) " :: Grateful Place"))
  [:h2 :.username] (h/content (:username user))
  [:.about]        (about-content user)

  [:.links :.posts :.count]    (h/content (str (relation-count user :post)))
  [:.links :.posts :a]         (h/set-attr :href (str "/users/" (:username user) "/posts"))
  
  [:.links :.comments :.count] (h/content (str (relation-count user :comment)))
  [:.links :.comments :a]      (h/set-attr :href (str "/users/" (:username user) "/comments")))