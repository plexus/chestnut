(ns chestnut.test.integration
  (:import [java.lang ProcessBuilder]
           [java.io InputStream OutputStream Writer Reader]
           [expect4j Expect4j])
  (:require [clojure.test :refer [deftest is run-tests]]
            [clojure.java.io :as io]
            [expect4clj :refer [glob-match eof-match regex-match expect]]))

(defn process-builder [& args]
  (ProcessBuilder. args))

(defn expect4j [process-map]
  (Expect4j. (:out process-map) (:in process-map)))

(defn set-dir! [process dir]
  (.directory process (io/file dir)))

(defn start-process [pb]
  (let [process (.start pb)]
    {:out (-> process
              (.getInputStream))
     :err (-> process
              (.getErrorStream))
     :in (-> process
             (.getOutputStream))
     :process process}))

(defn rm-rf [fname]
  (let [func (fn [func f]
               (when (.isDirectory f)
                 (doseq [f2 (.listFiles f)]
                   (func func f2)))
               (io/delete-file f))]
    (func func (io/file fname))))

(defn generate-app []
  (rm-rf "/tmp/sesame-seed")
  (-> (process-builder "lein" "new" "chestnut" "sesame-seed" "--snapshot")
      (set-dir! "/tmp")
      (start-process)
      ((juxt (comp slurp :out) (comp slurp :err)))))

(deftest generating-app
  (let [[out err] (generate-app)]
    (is (= "Generating fresh Chestnut project.\nREADME.md contains instructions to get you started.\n" out))
    (is (= "" err))))

#_(run-tests 'chestnut.test.integration)

(comment
  (-> process-map
      (expect4j)
      (expect
       (regex-match "README" [state]
                    (println "found README!"))))

  (defn prefix-writer [writer prefix]
    (proxy [Writer] []
      (write [bs]
        (doseq [line (clojure.string/split (str bs) #"\n")]
          (.write writer prefix)
          (.write writer line)
          (.write writer "\n")))
      (flush [] (.flush writer))))

  (defn inspect-input-stream [stream]
    (proxy [InputStream] []
      (read
        ([bytes]
         (print ".")
         (let [res (.read stream bytes)]
           (println (String. bytes))
           res))
        ([bytes off len]
         (print ".")
         (let [res (.read stream bytes)]
           (println (String. bytes off len))
           res))))))
