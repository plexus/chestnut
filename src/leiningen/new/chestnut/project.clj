(defproject {{full-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj"{{{project-source-paths}}}]

  :test-paths ["spec/clj"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2644" :scope "provided"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.2"]
                 [compojure "1.3.1"]
                 [enlive "1.1.5"]
                 [om "0.8.0-beta5"]
                 [environ "1.0.0"]{{{project-clj-deps}}}]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-environ "1.0.0"]{{{project-plugins}}}]

  :min-lein-version "2.5.0"

  :uberjar-name "{{{name}}}.jar"

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"{{{cljx-cljsbuild-spath}}}]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :source-map    "resources/public/js/out.js.map"
                                        :preamble      ["react/react.min.js"]
                                        :externs       ["react/externs/react.js"]
                                        :optimizations :none
                                        :pretty-print  true}}}}{{#less?}}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}{{/less?}}{{#sass?}}

  :sassc [{:src "src/scss/style.scss"
           :output-to "resources/public/css/style.css"}]
  :auto {"sassc"  {:file-pattern  #"\.(scss)$"}}{{/sass?}}

  :profiles {:dev {:source-paths ["env/dev/clj"]

                   :dependencies [[figwheel "0.1.6-SNAPSHOT"]
                                  [com.cemerick/piggieback "0.1.3"]
                                  [weasel "0.4.2"]
                                  [leiningen "2.5.0"]{{{project-dev-deps}}}]

                   :repl-options {:init-ns {{project-ns}}.server
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl{{{nrepl-middleware}}}]}

                   :plugins [[lein-figwheel "0.1.6-SNAPSHOT"]{{{project-dev-plugins}}}]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :css-dirs ["resources/public/css"]}

                   :env {:is-dev true}

                   :cljsbuild {:builds
                               {:app
                                {:source-paths ["env/dev/cljs"]}{{#speclj?}}
                                 :dev {:source-paths ["src/cljs"  "spec/cljs"]
                                       :compiler {:output-to     "resources/public/js/app_spec.js"
                                                  :output-dir    "resources/public/js/spec"
                                                  :source-map    "resources/public/js/spec.js.map"
                                                  :preamble      ["react/react.min.js"]
                                                  :externs       ["react/externs/react.js"]
                                                  :optimizations :whitespace
                                                  :pretty-print  false}
                                       :notify-command ["phantomjs"  "bin/speclj" "resources/public/js/app_spec.js"]}{{/speclj?}}}}{{#speclj?}}

                   :test-commands {"spec" ["phantomjs" "bin/speclj" "resources/public/js/app_spec.js"]}{{/speclj?}}{{#cljx?}}
                   :prep-tasks [["cljx" "once"] "javac" "compile"]

                   :cljx {:builds [{:source-paths ["src/cljx"]
                                    :output-path "target/generated/clj"
                                    :rules :clj}
                                   {:source-paths ["src/cljx"]
                                    :output-path "target/generated/cljs"
                                    :rules :cljs}]}{{/cljx?}}}

             :uberjar {:source-paths ["env/prod/clj"]
                       :hooks [{{{project-uberjar-hooks}}}]
                       :env {:production true}
                       :omit-source true
                       :aot :all
                       :cljsbuild {:builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}})
