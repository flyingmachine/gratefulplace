(ns gratefulplace.views.users
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :exclude [layout nav *template-dir*]]
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

;; TODO handle case where there are no posts
(defpage posts "users/posts.html"
  [user posts]
  [:title] (h/content (str (:username user) "'s Posts :: Grateful Place"))
  
  [:h2 :.username] (h/content (:username user))
  [[:.post         (h/nth-of-type 2)]] nil
  [:.post]         (h/clone-for [post posts]
                                [:.date]    (h/content (created-on post))
                                [:.content] (h/content (:content post))
                                [:a]        (set-post-path post))

  [:.links :.about :.username] (h/content (:username user))
  [:.links :.about :a]         (set-user-path user)

  [:.links :.comments :.count] (h/content (str (relation-count user :comment)))
  [:.links :.comments :a]      (h/set-attr :href (str "/users/" (:username user) "/comments")))