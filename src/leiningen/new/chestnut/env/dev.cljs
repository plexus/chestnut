(ns {{name}}.dev
    (:require [{{name}}.core :as core]
              [figwheel.client :as figwheel :include-macros true]
              [cljs.core.async :refer [put!]]
              [weasel.repl :as weasel]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback (fn [] (core/main)))

(weasel/connect "ws://localhost:9001" :verbose true)

(core/main)
