(ns {{project-ns}}.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [com.stuartsierra.component :as component]
            [org.clojars.featheredtoast.reloaded-repl-cljs :as reloaded]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defui RootComponent
  Object
  (render [this]
    (dom/div nil (dom/h1 nil (:text @app-state)))))

(def root (om/factory RootComponent))

(js/ReactDOM.render (root) (js/document.getElementById "app"))

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
