(ns gratefulplace.views.users
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :only [*template-dir* defpage]]))

(defpage show-new "users/new.html"
  [{:keys [attributes errors]}])