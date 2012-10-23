(ns gratefulplace.controllers.session
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.views.session :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only [*template-dir* nav]]))

(defn show-new
  []
  (view/show-new))