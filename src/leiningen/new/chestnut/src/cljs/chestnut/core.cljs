(ns {{project-ns}}.core
  (:require [om.core :as om :include-macros true]{{{core-cljs-requires}}}))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/h1 {{#not-om-tools?}}nil {{/not-om-tools?}}(:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))}))
