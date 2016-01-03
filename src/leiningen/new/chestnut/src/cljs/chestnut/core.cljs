(ns {{project-ns}}.core
  (:require [om.core :as om :include-macros true]{{{core-cljs-requires}}}))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn root-component [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil (dom/h1 {{#not-om-tools?}}nil {{/not-om-tools?}}(:text app))))))

(om/root
 root-component
 app-state
 {:target (. js/document (getElementById "app"))})
