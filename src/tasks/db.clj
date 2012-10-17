(ns tasks.db
  (:refer-clojure :exclude [alter drop complement
                            bigint boolean char double float time])
  (:require gratefulplace.models.user)
  (:use (lobos core)))

(defn rebuild
  []
  (rollback :all)
  (migrate))

(defn seed
  []
  (println (gratefulplace.models.user/create {:username "higginbotham"
                                              :email "daniel@flyingmachinestudios.com"
                                              :display_name "higginbotham"})))
(defn -main
  [task-name]
  (condp = task-name
    "rebuild" (rebuild)
    "seed" (seed)))