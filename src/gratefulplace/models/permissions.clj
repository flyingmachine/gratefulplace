(ns gratefulplace.models.permissions
  (:require [cemerick.friend :as friend]))

(defn current-username []
  (:username (friend/current-authentication)))

(defn current-user-id []
  (:id (friend/current-authentication)))

(defn modify-profile? [username]
  (= username (current-username)))

(defn modify-content? [user-id]
  (= user-id (current-user-id)))

;; Pretty sure there's something in onlisp about this
(defmacro protect [check & body]
  `(if (not ~check)
     (ring.util.response/redirect "/")
     (do ~@body)))


(def moderator-ids (clojure.string/split (get (System/getenv) "MODERATOR_NAMES" "daniel") #","))

(defn moderate? []
  (some #(= % (current-username)) moderator-ids))