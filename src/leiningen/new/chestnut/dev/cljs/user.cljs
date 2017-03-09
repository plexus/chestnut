(ns cljs.user
  (:require [{{project-ns}}.core]
            [{{project-ns}}.system :as system]))

(def go system/go)
(def reset system/reset)
(def stop system/stop)
(def start system/start)
