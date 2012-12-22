(ns gratefulplace.paths
  (use flyingmachine.webutils.paths))

(create-path-fns "user" :username "edit" "posts" "comments" "notification-settings")
(create-path-fns "post" :id "edit" "destroy")
(create-path-fns "favorite" :id "edit" "destroy")
(create-path-fns "comment" :id "edit" "destroy")
(create-path-fns "notification" :id)