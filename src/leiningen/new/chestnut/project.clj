(defproject {{full-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj"{{{project-source-paths}}}]

  :test-paths [{{{clj-test-src-path}}}]

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122" :scope "provided"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [slester/ring-browser-caching "0.1.1"]
                 [bk/ring-gzip "0.1.1"]
                 [compojure "1.4.0"]
                 [enlive "1.1.6"]
                 [org.omcljs/om "0.9.0"]
                 [environ "1.0.0"]{{{project-clj-deps}}}]

  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-environ "1.0.0"]{{{project-plugins}}}]

  :min-lein-version "2.5.0"

  :uberjar-name "{{{name}}}.jar"

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"{{{cljx-cljsbuild-spath}}}]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :source-map    "resources/public/js/out.js.map"
                                        :preamble      ["react/react.min.js"]
                                        :optimizations :none
                                        :pretty-print  true}}}}{{#less?}}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}{{/less?}}{{#sass?}}

  :sassc [{:src "src/scss/style.scss"
           :output-to "resources/public/css/style.css"}]
  :auto {"sassc"  {:file-pattern  #"\.(scss)$"}}{{/sass?}}{{#cljx?}}

  :prep-tasks [["cljx" "once"] "javac" "compile"]

  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/generated/clj"
                   :rules :clj}
                  {:source-paths ["src/cljx"]
                   :output-path "target/generated/cljs"
                   :rules :cljs}]}{{/cljx?}}

  :profiles {:dev {:source-paths ["env/dev/clj"]
                   :test-paths ["test/clj"]

                   :dependencies [[figwheel "0.3.9"]
                                  [figwheel-sidecar "0.3.9"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.10"]
                                  [weasel "0.7.0"]{{{project-dev-deps}}}]

                   :repl-options {:init-ns {{project-ns}}.server
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl{{{nrepl-middleware}}}]}

                   :plugins [[lein-figwheel "0.3.9"]{{{project-dev-plugins}}}]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :css-dirs ["resources/public/css"]
                              :ring-handler {{project-ns}}.server/http-handler}

                   :env {:is-dev true
                         :browser-caching {"text/javascript" 0
                                           "text/html" 0}}

                   :cljsbuild {:test-commands { {{{test-command-name}}} {{{test-command}}} }
                               :builds {:app {:source-paths ["env/dev/cljs"]}
                                        :test {:source-paths ["src/cljs" {{{cljs-test-src-path}}}]
                                               :compiler {:output-to     "resources/public/js/app_test.js"
                                                          :output-dir    "resources/public/js/test"
                                                          :source-map    "resources/public/js/test.js.map"
                                                          :preamble      ["react/react.min.js"]
                                                          :optimizations :whitespace
                                                          :pretty-print  false}
                                               :notify-command  ["phantomjs" "bin/speclj" "resources/public/js/app_test.js"]
                                               }}}}

             :uberjar {:source-paths ["env/prod/clj"]
                       :hooks [{{{project-uberjar-hooks}}}]
                       :env {:production true
                             :browser-caching {"text/javascript" 604800
                                               "text/html" 0}}
                       :omit-source true
                       :aot :all
                       :main {{project-ns}}.server
                       :cljsbuild {:builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}})
