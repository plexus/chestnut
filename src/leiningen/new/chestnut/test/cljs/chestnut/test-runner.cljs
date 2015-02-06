(ns {{sanitized}}.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [{{sanitized}}.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        '{{sanitized}}.core-test))
    0
    1))
