(ns {{project-ns}}.main
  (:require [{{project-ns}}.core :as core]
            [figwheel.client :as figwheel :include-macros true]
            [cljs.core.async :refer [put!]]))

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback (fn []
                     (core/main)))

(core/main)
