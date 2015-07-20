(ns {{sanitized}}.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [{{project-ns}}.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        '{{project-ns}}.core-test))
    0
    1))
