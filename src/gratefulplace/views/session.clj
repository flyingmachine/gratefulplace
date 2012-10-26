(ns gratefulplace.views.session
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :only [defpage]]))

(defpage show-new "session/new.html"
  [params]
  [:.errors] (if (:login_failed params)
               (h/content "We couldn't log you in with that username and password :("))
  [:input.username] (h/set-attr :value (:username params)))