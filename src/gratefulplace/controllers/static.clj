(ns gratefulplace.controllers.static
  (:require [gratefulplace.views.static :as view])
  (:use [gratefulplace.controllers.common :only (view)]))

(defn about
  [req]
  (view view/about))