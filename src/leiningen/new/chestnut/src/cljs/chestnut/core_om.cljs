(ns {{project-ns}}.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [com.stuartsierra.component :as component]
            [org.clojars.featheredtoast.reloaded-repl-cljs :as reloaded]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn root-component [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil (dom/h1 nil (:text app))))))

(om/root
 root-component
 app-state
 {:target (js/document.getElementById "app")})

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
