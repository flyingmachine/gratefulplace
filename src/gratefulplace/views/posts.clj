(ns gratefulplace.views.posts
  (:require [net.cgrand.enlive-html :as h]
            markdown)
  (:use [gratefulplace.views.common :exclude [layout nav *template-dir*]]
        gratefulplace.utils
        gratefulplace.models.permissions
        [cemerick.friend :only (current-authentication)]))

(defn comments
  [post]
  (let [comment-count (get-in post [:comment 0 :count] 0)]
    (if (zero? comment-count)
      "Comment"
      (str  comment-count " comments"))))

;; TODO refactor all this username access
(defpage all "index.html"
  [posts current-user]
  ;; don't show the second post as it's just an example
  [[:.post (h/nth-of-type 2)]] nil
  [:.post] (h/clone-for
            [post posts]
            [:.author :a]   (linked-username post)
            [:.date]     (h/content (created-on post))
            [:.content]  (md-content post)
            [:.comments] (h/do->
                          (h/content (comments post))
                          (h/set-attr :href (post-path post)))
            ;; TODO this is an unholy mess
            [:.favorite]
            (h/do->
             (h/add-class (when (and
                                 current-user
                                 (contains? (current-user-favorites (:id current-user)) (:id post)))
                            "added"))
             (h/set-attr :href
                         (if (and
                              current-user
                              (contains? (current-user-favorites (:id current-user)) (:id post)))
                                        
                           (str "/favorites/" (:id post) "/destroy")
                           (str "/favorites/" (:id post)))))))

(defpage show-new "posts/new.html"
  [attributes errors]
  [:#content :textarea] (h/content (:content attributes))
  [:#content :.errors] (if (current-authentication)
                         (error-content errors :content)
                         (h/html-content "You'll need to <a href=\"/login\">log in</a> to post")))

(defpage show "posts/show.html"
  [post comments current-user]
  [:.post :.author]      (linked-username post)
  [:.post :.date]        (h/content (created-on post))
  [:.post :.content]     (md-content post)

  [:.post :.edit]        (keep-when (can-modify-record? post))
  ;; TODO more path refactoring
  [:.post :.edit :a]     (h/set-attr :href (str "/posts/" (:id post) "/edit"))

  

  [:.post :.moderate]    (keep-when (moderator? (:username current-user)))
  [:.post :.moderate :a] (h/do->
                          (set-post-path post)
                          (h/content (if (:hidden post) "unhide" "hide")))
  
  [:#post_id]            (h/set-attr :value (:id post))

  [:.comments :.comment]
  (h/clone-for [comment comments]
               [:header]     (h/set-attr :id (str "comment-" (:id comment)))
               [:.author :a] (linked-username comment)
               [:.date]      (h/content (created-on comment))
               [:.content]   (md-content comment)

               [:.moderate]  (keep-when (moderator? (:username current-user)))
               [:.moderate :a] (h/do->
                                (set-comment-path comment)
                                (h/content (if (:hidden comment) "unhide" "hide")))

               [:.edit]      (keep-when (can-modify-record? comment current-user))
               ;; TODO more path refactoring
               [:.edit :a]   (h/set-attr :href (str "/comments/" (:id comment) "/edit")))

  [:.favorite]
  (h/do->
   (h/add-class (when (and
                       current-user
                       (contains? (current-user-favorites (:id current-user)) (:id post)))
                  "added"))
   (h/set-attr :href
               (if (and
                    current-user
                    (contains? (current-user-favorites (:id current-user)) (:id post)))
                 
                 (str "/favorites/" (:id post) "/destroy")
                 (str "/favorites/" (:id post))))))

(defpage edit "posts/edit.html"
  [post]
  [:form]     (h/set-attr :action (post-path post))
  [:textarea] (h/content  (:content post)))