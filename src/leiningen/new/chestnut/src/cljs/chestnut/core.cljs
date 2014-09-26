(ns {{name}}.core
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [figwheel.client :as figwheel :include-macros true]))

(def is-dev (some #{is-dev} (-> js/window (.-body) (.-classList))))

(defonce app-state (atom {:text "Hello world!"}))

(om/root
  (fn [app owner]
    (reify om/IRender
      (render [_]
        (dom/h1 nil (:text app)))))
  app-state
  {:target (. js/document (getElementById "app"))})

(if is-dev
  (enable-console-print!)
  (figwheel/watch-and-reload
   :websocket-url   "ws://localhost:3449/figwheel-ws"
   :jsload-callback (fn [] (print "reloaded"))))
