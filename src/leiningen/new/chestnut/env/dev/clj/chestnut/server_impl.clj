(ns {{project-ns}}.server-impl
  (:require [environ.core :refer [env]]
            [net.cgrand.enlive-html :refer [set-attr prepend append html]]
            [net.cgrand.reload :refer [auto-reload]]
            [figwheel-sidecar.repl-api :as figwheel]))

(def is-dev? (env :is-dev))

(def inject-devmode-html
  (comp
     (set-attr :class "is-dev")
     (prepend (html [:script {:type "text/javascript" :src "/js/out/goog/base.js"}]))
     (append  (html [:script {:type "text/javascript"} "goog.require('{{project-goog-module}}.main')"]))))


(def browser-repl figwheel/cljs-repl)

{{#less?}}
(defn start-less []
  (future
    (println "Starting less.")
    (clojure.java.shell/sh "lein" "less" "auto")))
{{/less?}}
{{#sass?}}
(defn start-sass []
  (future
    (println "Starting sass.")
    (clojure.java.shell/sh "lein" "auto" "sassc" "once")))
{{/sass?}}

(defn run []
  (auto-reload (find-ns '{{project-ns}}.server))
  (figwheel/start-figwheel!){{less-sass-start}})
