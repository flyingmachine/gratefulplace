(ns gratefulplace.controllers.session
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.views.session :as view]
            [cemerick.friend :as friend]))

(defn show-new
  []
  (view/show-new))