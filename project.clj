(defproject chestnut/lein-template "0.7.0-SNAPSHOT"
  :description "A Leiningen template for a ClojureScript setup with Figwheel, Austin, Om."
  :url "https://github.com/plexus/chestnut"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true

  :dependencies [[org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.github.jnr/jnr-process "1.0-SNAPSHOT"]
                 [clj-webdriver "0.7.0-SNAPSHOT"]]

  :aliases {"test" ["run" "-m" "chestnut.test.integration"] })
