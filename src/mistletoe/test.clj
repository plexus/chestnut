(ns mistletoe.test
  (:require [clojure.core.async :refer [go go-loop <!! <! >! chan put! alts!! timeout sliding-buffer]]))

(defn log-chan [process prefix & [key]]
  (let [key (or key :inChan)
        in (key process)
        out (chan 2)]
    (go-loop []
      (let [v (<! in)]
        (when v
          (>! out v)
          (if (re-seq #"\n$" v)
            (print prefix v)
            (println prefix v))
          (recur))))
    (assoc process key out)))

(defn expect [process regex & [ms]]
  (let [chan (:inChan process)]
    (loop []
      (let [timeout-chan (timeout (or ms 5000))
            [v ch] (alts!! [chan timeout-chan])]
        (cond
          (= ch timeout-chan) (throw
                               (java.util.concurrent.TimeoutException.
                                (str "Timeout waiting for " regex)))
          (nil? v) (throw
                    (Exception.
                     (str "Input channel closed, expecting " regex)))
          (re-seq regex v) (println "--> Got" v ", continuing.")
          :default (recur)))))
  process)
