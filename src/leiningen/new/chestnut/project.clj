(defproject {{full-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [{{{project-clj-deps}}}]

  :plugins [{{{project-plugins}}}]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs" "src/cljc"]

  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js" "dev-target"]

  :uberjar-name "{{{name}}}.jar"

  ;; Use `lein run` if you just want to start a HTTP server, without figwheel
  :main {{{project-ns}}}.application

  ;; nREPL by default starts in the :main namespace, we want to start in `user`
  ;; because that's where our development helper functions like (go) and
  ;; (browser-repl) live.
  :repl-options {:init-ns user}

  :cljsbuild {:builds
              [{:id "app"
                :source-paths ["src/cljs" "src/cljc" "dev"]

                :figwheel {:on-jsload "{{project-ns}}.system/reset"}

                :compiler {:main cljs.user
                           :asset-path "js/compiled/out"
                           :output-to "dev-target/public/js/compiled/{{{sanitized}}}.js"
                           :output-dir "dev-target/public/js/compiled/out"
                           :source-map-timestamp true}}

               {:id "test"
                :source-paths ["src/cljs" "test/cljs" "src/cljc" "test/cljc"]
                :compiler {:output-to "dev-target/public/js/compiled/testable.js"
                           :main {{{project-ns}}}.test-runner
                           :optimizations :none}}

               {:id "min"
                :source-paths ["src/cljs" "src/cljc"]
                :jar true
                :compiler {:main {{{project-ns}}}.system
                           :output-to "resources/public/js/compiled/{{{sanitized}}}.js"
                           :output-dir "target"
                           :source-map-timestamp true
                           :optimizations :advanced
                           :closure-defines {goog.DEBUG false}
                           :pretty-print false}}]}

  ;; When running figwheel from nREPL, figwheel will read this configuration
  ;; stanza, but it will read it without passing through leiningen's profile
  ;; merging. So don't put a :figwheel section under the :dev profile, it will
  ;; not be picked up, instead configure figwheel here on the top level.

  :figwheel {;; :http-server-root "public"       ;; serve static assets from resources/public/
             ;; :server-port 3449                ;; default
             ;; :server-ip "127.0.0.1"           ;; default
             :css-dirs ["resources/public/css"]  ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process. We
             ;; don't do this, instead we do the opposite, running figwheel from
             ;; an nREPL process, see
             ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
             ;; :nrepl-port 7888

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             :server-logfile "log/figwheel.log"}

  :doo {:build "test"}
  {{#less?}}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}
  {{/less?}}
  {{#sass?}}

  :sassc [{:src "src/scss/style.scss"
           :output-to "resources/public/css/style.css"}]

  :auto {"sassc" {:file-pattern  #"\.(scss)$"
                  :paths ["src/scss"]}}
  {{/sass?}}

  :profiles {:dev
             {:dependencies [{{{project-clj-dev-deps}}}]

              :plugins [{{{project-clj-dev-plugins}}}]

              :source-paths ["dev"]
              :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}

             :uberjar
             {:source-paths ^:replace ["src/clj" "src/cljc"]
              :prep-tasks [{{{project-prep-tasks}}}]
              :hooks [{{{project-uberjar-hooks}}}]
              :omit-source true
              :aot :all}})
