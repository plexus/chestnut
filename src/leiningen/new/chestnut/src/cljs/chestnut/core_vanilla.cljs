(ns {{project-ns}}.core
    (:require [rum.core :as rum]
              [com.stuartsierra.component :as component]
              [org.clojars.featheredtoast.reloaded-repl-cljs :as reloaded]))

(enable-console-print!)

(set! (.-innerHTML (js/document.getElementById "app"))
      "<h1>Hello Chestnut!</h1>")

(defrecord SampleComponent []
  component/Lifecycle
  (start [component]
    (println "start component")
    component)
  (stop [component]
    (println "stop component")
    component))
(defn new-sample-component []
  (map->SampleComponent {}))

(defn system []
  (component/system-map
   :sample-component (new-sample-component)))
(reloaded/set-init-go! #(system))
