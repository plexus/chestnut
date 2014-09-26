(ns {{name}}.core
  (:require [om.core :as om :include-macros true]
            [figwheel.client :as figwheel :include-macros true]
            [weasel.repl :as weasel]{{{core-cljs-requires}}}))

(defonce app-state (atom {:text "Hello Chestnut!"}))

(om/root
  (fn [app owner]
    (reify om/IRender
      (render [_]
        (dom/h1 nil (:text app)))))
  app-state
  {:target (. js/document (getElementById "app"))})

(def is-dev (.contains (.. js/document -body -classList) "is-dev"))

(when is-dev
  (enable-console-print!)
  (figwheel/watch-and-reload
   :websocket-url "ws://localhost:3449/figwheel-ws"
   :jsload-callback (fn [] (print "reloaded")))
  (weasel/connect "ws://localhost:9001" :verbose true))
