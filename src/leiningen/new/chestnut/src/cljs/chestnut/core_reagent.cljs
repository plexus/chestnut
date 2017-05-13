(ns {{project-ns}}.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn greeting []
  [:h1 (:text @app-state)])

(defn render []
  (reagent/render [greeting] (js/document.getElementById "app")))
