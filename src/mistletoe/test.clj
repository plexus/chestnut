(ns mistletoe.test
  (:import [java.util.concurrent TimeoutException])
  (:require [clojure.core.async :refer [go-loop <! >! chan
                                        alts!! timeout close!]]))


(def ^:dynamic *expect-guard-for* #{})

(defn log-chan [process prefix & [key]]
  (let [key (or key :inChan)
        in (key process)
        out (chan 2)]
    (go-loop []
      (let [v (<! in)]
        (if v
          (do
            (>! out v)
            (if (re-seq #"\n$" v)
              (print prefix v)
              (println prefix v))
            (recur))
          (close! out))))
    (assoc process key out)))

(defn expect [process regex & [timeout-sec]]
  (loop []
    (let [chan (:inChan process)
          ms (* 1000 (or timeout-sec 30))
          timeout-chan (timeout ms)
          [v ch] (alts!! [chan timeout-chan])]
      (cond
        (= ch timeout-chan)
          (throw (TimeoutException. (str "Timeout waiting for " regex)))
        (nil? v)
          (throw (Exception. (str "Input channel closed, expecting " regex)))
        (re-seq regex v)
          (println "--> Got" v ", continuing.")
        (first (filter #(re-seq % v) *expect-guard-for*))
          (throw (Exception. (str "Got unexpected input: "
                                  (first (filter #(re-seq % v) *expect-guard-for*)) " in " v)))
        :default
          (recur))))
  process)
