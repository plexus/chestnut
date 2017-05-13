(ns {{project-ns}}.env
  (:require [environ.core :refer [env]]))

(defmacro cljs-env [kw]
  (env kw))
