(ns user
  (:require [{{project-ns}}.server]
            [ring.middleware.reload :refer [wrap-reload]]
            [figwheel-sidecar.repl-api :as figwheel]{{#less?}}
            [clojure.java.shell]{{/less?}}{{#sass?}}
            [clojure.java.shell]{{/sass?}}))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed){{#less?}}

(defn start-less []
  (future
    (println "Starting less.")
    (clojure.java.shell/sh "lein" "less" "auto")))
{{/less?}}{{#sass?}}

(defn start-sass []
  (future
    (println "Starting sass.")
    (clojure.java.shell/sh "lein" "auto" "sassc" "once")))
{{/sass?}}

(def http-handler
  (wrap-reload #'{{project-ns}}.server/http-handler))

(defn run []
  (figwheel/start-figwheel!){{less-sass-start}})

(def browser-repl figwheel/cljs-repl)
