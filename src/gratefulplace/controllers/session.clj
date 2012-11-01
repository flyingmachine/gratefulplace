(ns gratefulplace.controllers.session
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.views.session :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only (if-valid view)]))

(defn show-new
  [req]
  (view view/show-new))