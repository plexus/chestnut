(ns {{project-ns}}.system
  (:require [com.stuartsierra.component :as component]
            [{{project-ns}}.components.ui :refer [new-ui-component]]))

(declare system)

(defn new-system []
  (component/system-map
   :app-root (new-ui-component)))

(defn init []
  (set! system (new-system)))

(defn start []
  (set! system (component/start system)))

(defn stop []
  (set! system (component/stop system)))

(defn ^:export go []
  (init)
  (start))

(defn reset []
  (stop)
  (go))
