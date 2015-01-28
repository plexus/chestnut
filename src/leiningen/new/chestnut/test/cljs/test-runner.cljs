(ns test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [{{sanitized}}.test.core]))


(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        '{{sanitized}}.test.core))
    0
    1))
