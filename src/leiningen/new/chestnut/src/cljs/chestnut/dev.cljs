(ns {{name}}.dev
    (:require [figwheel.client :as figwheel :include-macros true]
              [weasel.repl :as weasel]))

(def is-dev? (.contains (.. js/document -body -classList) "is-dev"))

(when is-dev?
  (enable-console-print!)
  (figwheel/watch-and-reload
   :websocket-url "ws://localhost:3449/figwheel-ws"
   :jsload-callback (fn [] (print "reloaded")))
  (weasel/connect "ws://localhost:9001" :verbose true))
