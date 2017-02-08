(ns {{project-ns}}.core
  (:require [rum.core :as rum]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(rum/defc greeting < rum/reactive []
   [:h1 (:text (rum/react app-state))])

(rum/mount (greeting) (. js/document (getElementById "app")))
