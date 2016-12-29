(ns user
  (:require [{{project-ns}}.application]
            [com.stuartsierra.component :as component]
            [figwheel-sidecar.config :as fw-config]
            [figwheel-sidecar.system :as fw-sys]
            [clojure.tools.namespace.repl :refer [set-refresh-dirs]]
            [reloaded.repl :refer [system init start stop go reset reset-all]]
            [ring.middleware.reload :refer [wrap-reload]]
            [figwheel-sidecar.repl-api :as figwheel]{{#less?}}            [clojure.java.shell]{{/less?}}{{#sass?}}            [clojure.java.shell]{{/sass?}}))

(defn dev-system []
  (merge
   ({{project-ns}}.application/app-system)
   (component/system-map
    :figwheel-system (fw-sys/figwheel-system (fw-config/fetch-config))
    :css-watcher (fw-sys/css-watcher {:watch-paths ["resources/public/css"]}))))
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

(set-refresh-dirs "src" "dev")
(reloaded.repl/set-init! #(dev-system))

(defn run []
  (go){{less-sass-start}})

(defn browser-repl []
  (fw-sys/cljs-repl (:figwheel-system system)))
