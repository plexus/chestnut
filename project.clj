(defproject chestnut/lein-template "0.7.0-SNAPSHOT"
  :description "A Leiningen template for a ClojureScript setup with Figwheel, Austin, Om."
  :url "https://github.com/plexus/chestnut"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true

  :dependencies [[com.github.cverges.expect4j/expect4j "1.2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.github.jnr/jnr-process "1.0-SNAPSHOT"]
                 [clj-webdriver "0.7.0-SNAPSHOT"]
                 [environ "1.0.0"]]

  :repositories [["expect4j" "https://github.com/cverges/expect4j/raw/mvn-repo"]]

  :aliases {"test" ["run" "-m" "chestnut.test.integration"] })
