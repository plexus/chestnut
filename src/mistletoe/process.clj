(ns mistletoe.process
  (:import [java.nio ByteBuffer]
           [jnr.constants.platform Signal])
  (:require [clojure.core.async :refer [go-loop <! >! chan close!]]))

(defn process [& args]
  (let [argsAry (into-array String args)
        process (jnr.process.ProcessBuilder. argsAry)]
    {:processBuilder process
     :inBuf (ByteBuffer/allocate 2048)
     :outBuf (ByteBuffer/allocate 2048)}))

(defn directory [processMap dir]
  (.directory (:processBuilder processMap) dir)
  processMap)

(defn start [processMap]
  (let [process (.start (:processBuilder processMap))]
    (merge
     processMap
     {:process process
      :in (.getIn process)
      :err (.getErr process)
      :out (.getOut process)})))

(defn read-str [process & [stream]]
  (let [buf (:inBuf process)
        chan ((or stream :in) process)]
    (.clear buf)
    (.read chan buf)
    (let [len (.position buf)
          bytes (.array buf)]
      (if (> len 0)
        (String. (into-array Byte/TYPE (take len bytes)))))))

(defn read-err [process]
  (read-str process :err))

(defn write-str [process exp]
  (let [buf (:outBuf process)
        chan (:out process)]
    (.clear buf)
    (.put buf (into-array Byte/TYPE (str exp)))
    (.flip buf)
    (.write chan buf))
  process)

(defn start-pipe [process & [stream]]
  (let [c (chan 1)
        stream (or stream :in)]
    (go-loop []
      (if-let [str (read-str process stream)]
        (>! c str)
        (close! c))
      (recur))
    (assoc process (keyword (str (name stream) "Chan")) c)))

(defn kill [{process :process} & [signal]]
  (.kill process (or signal Signal/SIGKILL)))

(defn posix []
  (jnr.posix.POSIXFactory/getPOSIX))

(defn kill-process-group [{process :process} & [signal]]
  (.kill (posix)
         (- (.getPid process))
         (.intValue (or signal Signal/SIGKILL))))

(defn wait-for [{process :process}]
  (.waitFor process))

(defn chdir
  "Change the working directory of the running process (the JVM)"
  [dir]
  (.chdir (posix) dir))
