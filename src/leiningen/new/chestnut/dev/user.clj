(ns user
  (:require [{{project-ns}}.application]
            [com.stuartsierra.component :as component]
            [suspendable.core :refer [Suspendable]]
            [figwheel-sidecar.config :as fw-config]
            [figwheel-sidecar.system :as fw-sys]
            [clojure.tools.namespace.repl :refer [set-refresh-dirs]]
            [reloaded.repl :refer [system init]]
            [ring.middleware.reload :refer [wrap-reload]]
            [figwheel-sidecar.repl-api :as figwheel]{{user-clj-requires}}
            [{{project-ns}}.config :refer [config]])
  (:import [figwheel_sidecar.system FigwheelSystem]))

(extend-type FigwheelSystem
  Suspendable
  (suspend [c] c)
  (resume [c old-c]
    (-> c
        (assoc :system-running (:system-running old-c))
        (assoc :system (:system old-c)))))

(defn dev-system []
  (assoc ({{project-ns}}.application/app-system (config))
    :figwheel-system (fw-sys/figwheel-system (fw-config/fetch-config))
    :css-watcher (fw-sys/css-watcher {:watch-paths ["resources/public/css"]}){{{extra-dev-components}}}))

(set-refresh-dirs "src" "dev")
(reloaded.repl/set-init! #(dev-system))

(defn cljs-repl []
  (fw-sys/cljs-repl (:figwheel-system system)))

;; Set up aliases so they don't accidentally
;; get scrubbed from the namespace declaration
(def start reloaded.repl/start)
(def stop reloaded.repl/stop)
(def go reloaded.repl/go)
(def reset reloaded.repl/reset)
(def reset-all reloaded.repl/reset-all)

;; deprecated, to be removed in Chestnut 1.0
(defn run []
  (println "(run) is deprecated, use (go)")
  (go))

(defn browser-repl []
  (println "(browser-repl) is deprecated, use (cljs-repl)")
  (cljs-repl))
