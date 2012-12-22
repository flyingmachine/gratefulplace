(ns gratefulplace.controllers.session
  (:require [ring.util.response :as res]
            [gratefulplace.views.session :as view])

  (:use gratefulplace.controllers.common))

(defn show-new
  [params]
  (view view/show-new))