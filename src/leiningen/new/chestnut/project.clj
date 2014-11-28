(defproject {{full-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs" {{{cljx-source-paths}}}]
  :test-paths ["spec/clj"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371" :scope "provided"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha" :scope "provided"]
                 [ring "1.3.1"]
                 [compojure "1.2.0"]
                 [enlive "1.1.5"]
                 [om "0.7.3"]
                 [figwheel "0.1.4-SNAPSHOT"]
                 [environ "1.0.0"]
                 [com.cemerick/piggieback "0.1.3"]
                 [weasel "0.4.0-SNAPSHOT"]
                 [leiningen "2.5.0"]{{{project-clj-deps}}}]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ancient "0.5.4"]
            [lein-environ "1.0.0"] {{{spec-plugin}}} {{{less-plugin}}} {{{sass-plugin}}}]

  :min-lein-version "2.5.0"

  :uberjar-name "{{name}}.jar"

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"{{{cljx-cljsbuild-spath}}}]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :source-map    "resources/public/js/out.js.map"
                                        :preamble      ["react/react.min.js"]
                                        :externs       ["react/externs/react.js"]
                                        :optimizations :none
                                        :pretty-print  true}}
                        {{#spec?}}
                        :dev {:source-paths ["src/cljs"  "spec/cljs"]
                              :compiler {:output-to     "resources/public/js/app_spec.js"
                                         :output-dir    "resources/public/js/spec"
                                         :source-map    "resources/public/js/spec.js.map"
                                         :preamble      ["react/react.min.js"]
                                         :externs       ["react/externs/react.js"]
                                         :optimizations :whitespace
                                         :pretty-print  false}
                               :notify-command ["phantomjs"  "bin/speclj" "resources/public/js/app_spec.js"]}
                        {{/spec?}}}
              {{#spec?}}
              :test-commands {"test" ["phantomjs" "bin/speclj" "resources/public/js/app_spec.js"]}
              {{/spec?}}}
  {{#less?}}
  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}
  {{/less?}}

  {{#sass?}}
  :sassc [{:src "src/scss/style.scss"
           :output-to "resources/public/css/style.css"}]
  :auto {"sassc"  {:file-pattern  #"\.(scss)$"}}
  {{/sass?}}

  :profiles {:dev {:repl-options {:init-ns {{project-ns}}.server
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl{{{nrepl-middleware}}}]}

                   :plugins [[lein-figwheel "0.1.4-SNAPSHOT"]{{{project-dev-plugins}}}]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :css-dirs ["resources/public/css"]}
                   :env {:is-dev true}

                   {{#spec?}}
                   :dependencies [[speclj "3.1.0"]]
                   {{/spec?}}

                   {{#cljx-hook?}}
                   :hooks [cljx.hooks]
                   {{/cljx-hook?}}
                   {{#cljx-build?}}
                   :cljx {:builds [{:source-paths ["src/cljx"]
                                    :output-path "target/generated/clj"
                                    :rules :clj}
                                   {:source-paths ["src/cljx"]
                                    :output-path "target/generated/cljs"
                                    :rules :cljs}]}
                   {{/cljx-build?}}
                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]}}}}

             :uberjar {:hooks [{{cljx-uberjar-hook}}leiningen.cljsbuild{{less-hook}}{{sass-hook}}]
                       :env {:production true}
                       :omit-source true
                       :aot :all
                       :cljsbuild {:builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}})
