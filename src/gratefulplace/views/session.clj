(ns gratefulplace.views.session
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :only [defpage]]))

(defpage show-new "session/new.html"
  [])