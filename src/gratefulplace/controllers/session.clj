(ns gratefulplace.controllers.session
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only [*template-dir* nav]]))