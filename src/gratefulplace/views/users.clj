(ns gratefulplace.views.users
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :exclude [layout nav *template-dir*]]
        gratefulplace.utils
        gratefulplace.models.permissions))

(defpage show-new "users/new.html"
  [params errors]
  [:#username :input] (h/set-attr :value (:username params))
  [:#username :.errors] (error-content errors :username)
  
  [:#password :.errors] (error-content errors :password)
  
  [:#email :input] (h/set-attr :value (:email params))
  [:#email :.errors] (error-content errors :email))


;; TODO if this is your page, show edit link
(defn about-content
  [user]
  (md-content
   (let [about (:about user)]
     (self-unless-fn about empty?
       "This user hasn't customized their profile yet."))))

(defn local-nav
  [node user]
  (h/at node
        [:.about :a]         (set-path user user-path)
        
        [:.posts :.count]    (h/content (str (relation-count user :post)))
        [:.posts :a]         (set-path user user-posts-path)
        
        [:.comments :.count] (h/content (str (relation-count user :comment)))
        [:.comments :a]      (set-path user user-comments-path)))

(defpage show "users/show.html"
  [user]
  [:title]         (h/content (str "About " (:username user) " :: Grateful Place"))
  [:h2 :.username] (h/content (:username user))
  [:div.about]     (about-content user)

  [:.edit]    (keep-when (can-modify-profile? user))
  [:.edit :a] (set-path user user-edit-path)
  
  [:.local-nav]    #(local-nav % user))

;; TODO handle case where there are no posts
(defpage posts "users/posts.html"
  [user posts]
  [:title] (h/content (str (:username user) "'s Posts :: Grateful Place"))
  
  [:h2 :.username] (h/content (:username user))
  [[:div.post         (h/nth-of-type 2)]] nil
  [:div.post]         (h/clone-for [post posts]
                                [:.date]    (h/content (created-on post))
                                [:.content] (h/content (:content post))
                                [:a]        (set-path post post-path))

  [:.local-nav]    #(local-nav % user))


(defpage comments "users/comments.html"
  [user comments]
  [:title] (h/content (str (:username user) "'s Comments :: Grateful Place"))
  
  [:h2 :.username] (h/content (:username user))
  [[:.comment         (h/nth-of-type 2)]] nil
  [:.comment]         (h/clone-for [comment comments]
                                [:.date]    (h/content (created-on comment))
                                [:.content] (h/content (:content comment))
                                [:a]        (set-path (:post_id comment) post-path))
  
  [:.local-nav]    #(local-nav % user))

(defpage edit "users/edit.html"
  [user errors]

  [:textarea] (h/content (:about user))
  [:form] (h/set-attr :action (user-path user))

  [:.edit :a] (set-path user user-path)

  [:#change-password :.errors] (error-content errors :change-password)

  [:#change-email [:input (h/attr= :type "text")]] (h/set-attr :value (:email user))
  [:#change-email :.errors]    (error-content errors :email))