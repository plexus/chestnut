(ns {{project-ns}}.core
    (:require [rum.core :as rum]
              [com.stuartsierra.component :as component]
              [org.clojars.featheredtoast.reloaded-repl-cljs :as reloaded]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(rum/defc greeting < rum/reactive []
   [:h1 (:text (rum/react app-state))])

(rum/mount (greeting) (. js/document (getElementById "app")))

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
