(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [ring "1.2.2"]
                 [compojure "1.1.8"]
                 [enlive "1.1.5"]
                 [om "0.7.1"]
                 [figwheel "0.1.3-SNAPSHOT"]
                 [environ "0.5.0"]]

  :plugins [[com.cemerick/austin "0.1.5-SNAPSHOT"]
            [lein-cljsbuild "1.0.3"]]

  :min-lein-version "2.0.0"

  :uberjar-name "{{sanitized}}.jar"

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/app.js"
                                        :output-dir    "resources/public/out"
                                        :source-map    "resources/public/out.js.map"
                                        :preamble      ["react/react.min.js"]
                                        :externs       ["react/externs/react.js"]
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :profiles {:dev {:repl-options {:init-ns {{name}}.server}
                   :plugins [[lein-figwheel "0.1.3-SNAPSHOT"]]
                   :figwheel {:http-server-root "public"
                              :port 3449 }}

             :uberjar {:hooks [leiningen.cljsbuild]
                       :env {:production true}
                       :omit-source true
                       :aot :all
                       :cljsbuild {:builds {:app
                                            {:compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}})
