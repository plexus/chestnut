(ns mistletoe.test
  (:import [java.util.concurrent TimeoutException])
  (:require [clojure.core.async :refer [go go-loop <!! <! >! chan put!
                                        alts!! timeout sliding-buffer]]))


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

(defn guard-for [process regex & [key]]
  (let [key (or key :inChan)
        in (key process)
        out (chan 2)]
    (go-loop []
      (let [v (<! in)]
        (when v
          (>! out v)
          (when (re-seq regex v)
            (throw (Exception. (str "Got unexpected input: " regex " in " v))))
          (recur))))
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
        :default
          (recur))))
  process)
