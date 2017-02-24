(ns cljs.user
  (:require [{{project-ns}}.core]
            [org.clojars.featheredtoast.reloaded-repl-cljs :as reloaded]))

(def go reloaded/go)
(def reset reloaded/reset)
(def stop reloaded/stop)
(def start reloaded/start)
