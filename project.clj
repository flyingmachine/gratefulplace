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
                 [korma "0.3.0-beta9"]
                 [lobos "1.0.0-SNAPSHOT"]
                 [postgresql "9.1-901.jdbc4"]
                 [com.cemerick/friend "0.1.2"]
                 [markdown-clj "0.9.9"]]
  
  :main gratefulplace.server)


