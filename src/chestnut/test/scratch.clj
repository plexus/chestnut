(def repl (-> (posix-spawn ["lein" "repl"] [] "/tmp/sesame-seed")
              (make-process-map)))

;; (def repl (start-repl))
(def in-chan (Channels/newChannel (:out repl)))
(def in-buf (ByteBuffer/allocate 2048))

(def selector (Selector/open))

(def out-chan (Channels/newChannel (:in repl)))
(def out-buf (ByteBuffer/allocate 2048))

(let [buf (java.nio.ByteBuffer/allocate 2048)]
  (defn read-next [chan]
    (.clear buf)
    (.read chan buf)
    (String. (into-array Character/TYPE (take (.position buf) (.array buf))))))

(let [buf (java.nio.ByteBuffer/allocate 2048)]
  (defn write-next [chan exp]
    (.clear buf)
    (.put buf (into-array Byte/TYPE (str exp)))
    (.flip buf)
    (.write chan buf)))

(def repl-process
  (-> (jnr.process.ProcessBuilder. (into-array ["lein" "repl"]))
      (.directory "/tmp/sesame-seed")
      (.start)))

(.configureBlocking (.getIn repl-process) false)
(.configureBlocking (.getErr repl-process) false)

(def selector (.openSelector (jnr.enxio.channels.NativeSelectorProvider/getInstance)))
(def selection-key (.register (.getIn repl-process) selector java.nio.channels.SelectionKey/OP_READ))
(def selection-key (.register (.getErr repl-process) selector java.nio.channels.SelectionKey/OP_READ))

(.selectNow selector)

(read-next (.channel (first (.selectedKeys selector))))
(write-next (.getOut repl-process) "(+ 1 1)\n")

(expect #"2")

(defn expect [regex]
  )



(def repl-process
  (-> (process "lein" "repl")
      (directory "/tmp/sesame-seed")
      (start)))
