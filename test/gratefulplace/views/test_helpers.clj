(ns gratefulplace.views.test-helpers
  (:require [net.cgrand.enlive-html :as h])
  (:use [clojure.contrib.string :only (substring?)]))

(defn snippet->str
  [enlive-snippet]
  (apply str (h/emit* enlive-snippet)))

(defn snippet-contains?
  [substring snippet]
  (substring? substring (snippet->str snippet)))

(defn attr-set?
  [node attr val]
  (= (get-in node [:attrs attr]) val))

(defn content-set?
  [node val]
  (= (apply str (snippet->str node)) val))