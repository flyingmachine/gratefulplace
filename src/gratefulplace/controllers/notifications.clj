(ns gratefulplace.controllers.notifications
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.comment-notification :as notification]
            [gratefulplace.views.notifications :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only (if-valid view)]
        gratefulplace.models.permissions))

(defn all
  [params]
  (view
   view/all
   :notifications (if (current-user-id)
                    (let [html (notification/all (current-user-id))]
                      (notification/mark-all-viewed (current-user-id))
                      html)
                    [])))

;; This kind of interesting because it serves both as an MVC
;; controller for serving views *and* as a controller for coordinating
;; the creation and sending of notifications