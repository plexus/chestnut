(defproject chestnut/lein-template "0.19.0-SNAPSHOT"
  :description "A Leiningen template for a minimal but complete Clojure/ClojureScript setup."
  :url "https://github.com/plexus/chestnut"
  :jar-inclusions [#"leiningen/new/chestnut/\.gitignore"
                   #"leiningen/new/chestnut/\.dir-locals\.el"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :eval-in-leiningen true

  :dependencies [[com.github.plexus/clj-jgit "v0.8.9-preview"]
                 [org.slf4j/slf4j-nop "1.7.25"]
                 [org.apache.httpcomponents/httpclient "4.5.3"]
                 ;;[re-frame/lein-template "0.2.7-1"]
                 [com.github.plexus/re-frame-template "v0.2.7-1-indent-fix"]
                 [ancient-clj "0.6.15"]
                 [clj-http "3.9.1"]]


  :profiles {:test {:dependencies [[org.clojure/core.async "0.3.443"]
                                   [com.github.jnr/jnr-process "1.0-SNAPSHOT"]
                                   [clj-webdriver "0.7.2"]]}}

  :aliases {"test" ["with-profile" "+test" "run" "-m" "chestnut.test.integration"] }

  :repositories [["jitpack" "https://jitpack.io"]])
