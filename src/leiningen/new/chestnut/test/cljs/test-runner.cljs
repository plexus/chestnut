(ns test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [foobar.test.core]))


(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'foobar.test.core))
    0
    1))
