(ns gratefulplace.controllers.common
  (:require [net.cgrand.enlive-html :as h]))

(defonce *template-dir* "gratefulplace/templates/")

(h/defsnippet nav (str *template-dir* "index.html") [:nav]
  [logged-in]
  [:.auth] (h/content (if logged-in "Log Out" "Log In")))

(h/deftemplate layout (str *template-dir* "index.html")
  [html]
  [:html] html
  [:nav] (nav false))