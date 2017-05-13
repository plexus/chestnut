(ns {{project-ns}}.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defui RootComponent
  Object
  (render [this]
    (dom/div nil (dom/h1 nil (:text @app-state)))))

(def root (om/factory RootComponent))

(defn render []
  (js/ReactDOM.render (root) (js/document.getElementById "app")))
