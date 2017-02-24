(ns {{project-ns}}.core
    (:require [reagent.core :as reagent :refer [atom]]
              [com.stuartsierra.component :as component]
              [org.clojars.featheredtoast.reloaded-repl-cljs :as reloaded]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn greeting []
  [:h1 (:text @app-state)])

(reagent/render [greeting] (js/document.getElementById "app"))

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
