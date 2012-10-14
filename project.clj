(defproject gratefulplace "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.1.6"]
                 [compojure "1.1.3"]
                 [enlive/enlive "1.0.1"]
                 [lein-ring "0.7.5"]
                 [korma "0.3.0-beta7"]
                 [postgresql "9.1-901.jdbc4"]
                 [ragtime "0.3.1"]]

  :dev-dependencies [[ragtime "0.3.1"]]

  :plugins [[ragtime/ragtime.lein "0.3.1"]]
  
  :main gratefulplace.server

  :ragtime {:migrations ragtime.sql.files/migrations
            :database "jdbc:postgresql://localhost/gratefulplace?user=daniel&password="})
