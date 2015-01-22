(ns mistletoe.process
  (:import
   [java.nio ByteBuffer])
  (:require
   [clojure.core.async :refer [go-loop <!! <! >! chan put! sliding-buffer timeout]]))

(defn process [& args]
  (jnr.process.ProcessBuilder. (into-array String args)))

(defn directory [process dir]
  (.directory process dir))

(defn start [process]
  (.start process))

(defn read-str [chan buf]
  (.clear buf)
  (.read chan buf)
  (String. (into-array Character/TYPE (take (.position buf) (.array buf)))))

(defn write-str [chan buf exp]
  (.clear buf)
  (.put buf (into-array Byte/TYPE (str exp)))
  (.flip buf)
  (.write chan buf))

(comment
  (def c (chan))
  (def buf (ByteBuffer/allocate 2048))
  (def out-buf (ByteBuffer/allocate 2048))


  (go-loop []
    (>! c (read-str (.getIn repl-process) buf))
    (recur))

  (go-loop []
    (>! c (read-str (.getErr repl-process) buf))
    (recur))

  (go-loop []
    (println "Got" (<! c))
    (recur)))

(write-str (.getOut repl-process) out-buf "(run)\n")
