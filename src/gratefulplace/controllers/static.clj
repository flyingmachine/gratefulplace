(ns gratefulplace.controllers.static
  (:require [gratefulplace.views.static :as view])
  (:use gratefulplace.controllers.common))

(defn about
  [params]
  (view view/about))