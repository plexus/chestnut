(defproject chestnut/lein-template "0.14.0"
  :description "A Leiningen template for a ClojureScript setup with Figwheel, Om."
  :url "https://github.com/plexus/chestnut"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true

  :dependencies [[com.github.plexus/clj-jgit "v0.8.9-preview"]
                 [org.slf4j/slf4j-nop "1.7.19"]]

  :profiles {:test {:dependencies [[org.clojure/core.async "0.2.374"]
                                   [com.github.jnr/jnr-process "1.0-SNAPSHOT"]
                                   [clj-webdriver "0.7.2"]]}}

  :aliases {"test" ["with-profile" "+test" "run" "-m" "chestnut.test.integration"] }

  :repositories [["jitpack" "https://jitpack.io"]])
