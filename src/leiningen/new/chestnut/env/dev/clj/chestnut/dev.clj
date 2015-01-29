(ns {{project-ns}}.dev
  (:require [environ.core :refer [env]]
            [net.cgrand.enlive-html :refer [set-attr prepend append html]]
            [cemerick.piggieback :as piggieback]
            [weasel.repl.websocket :as weasel]
            [figwheel-sidecar.auto-builder :as fig-auto]
            [figwheel-sidecar.core :as fig]
            [clojurescript-build.auto :as auto]))

(def is-dev? (env :is-dev))

(def inject-devmode-html
  (comp
     (set-attr :class "is-dev")
     (prepend (html [:script {:type "text/javascript" :src "/js/out/goog/base.js"}]))
     (prepend (html [:script {:type "text/javascript" :src "/react/react.js"}]))
     (append  (html [:script {:type "text/javascript"} "goog.require('{{project-goog-module}}.main')"]))))

(defn browser-repl []
  (let [repl-env (weasel/repl-env :ip "0.0.0.0" :port 9001)]
    (piggieback/cljs-repl :repl-env repl-env)
    (piggieback/cljs-eval repl-env '(in-ns '{{project-ns}}.core) {})))

(defn start-figwheel []
  (let [server (fig/start-server { :css-dirs ["resources/public/css"] })
        config {:builds [{:source-paths ["env/dev/cljs" "src/cljs"]
                          :compiler {:output-to     "resources/public/js/app.js"
                                     :output-dir    "resources/public/js/out"
                                     :source-map    "resources/public/js/out.js.map"
                                     :preamble      ["react/react.min.js"]}}]
                :figwheel-server server}]
    (fig-auto/autobuild* config)))
{{#less?}}
(defn start-less []
  (future
    (println "Starting less.")
    (lein/-main ["less" "auto"])))
{{/less?}}
{{#sass?}}
(defn start-sass []
  (future
    (println "Starting sass.")
    (lein/-main ["auto" "sassc" "once"])))
{{/sass?}}
