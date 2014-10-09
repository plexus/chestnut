(ns {{name}}.dev
    (:require [figwheel.client :as figwheel :include-macros true]
              [cljs.core.async :refer [put!]]
              [weasel.repl :as weasel]))

(def is-dev? (.contains (.. js/document -body -classList) "is-dev"))

(defn setup [re-render-ch]
  (when is-dev?
    (enable-console-print!)

    (figwheel/watch-and-reload
     :websocket-url "ws://localhost:3449/figwheel-ws"
     :jsload-callback (fn []
                        (println "reloaded")
                        (put! re-render-ch true)))

    (weasel/connect "ws://localhost:9001" :verbose true)))
