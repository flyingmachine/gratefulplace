(ns tasks.db
  (:refer-clojure :exclude [alter drop complement
                            bigint boolean char double float time])
  (:require [gratefulplace.models.user :as user])
  (:use (lobos core connectivity)))

(defn rebuild
  []
  (open-global :lobos gratefulplace.models.db/db-config)
  (rollback :all)
  (migrate))

(defn seed
  []
  (println (user/create! {:username     "higginbotham"
                          :email        "daniel@flyingmachinestudios.com"
                          :display_name "higginbotham"
                          :password     "password"})))
(defn -main
  [task-name]
  (condp = task-name
    "rebuild" (rebuild)
    "seed" (seed)))