(ns chestnut.test.integration
  (:require [clojure.java.io :as io]
            [mistletoe.process :refer :all]
            [mistletoe.test :refer :all]))

(defn rm-rf [fname]
  (if (= fname "/")
    (throw (Exception. "Refusing to 'rm -rf /'")))
  (let [func (fn [func f]
               (when (.isDirectory f)
                 (doseq [f2 (.listFiles f)]
                   (func func f2)))
               (io/delete-file f))]
    (func func (io/file fname))))


(defn test-chestnut []
  (rm-rf "/tmp/sesame-seed")

  (println "--> Generating app")

  (-> (process "lein" "new" "chestnut" "sesame-seed" "--snapshot")
      (directory "/tmp")
      (start)
      (start-pipe :in)
      (start-pipe :err)
      (log-chan "lein new       | ")
      (log-chan "lein new (err) | ")
      (waitFor))

  (println "--> Starting REPL")

  #_(-> (process "lein" "repl")
      (directory "/tmp/sesame-seed")
      (start)
      (start-pipe :in)
      (start-pipe :err)
      (log-chan "REPL        | " (start-pipe repl))
      (log-chan "REPL (err)  | " (start-pipe repl :err) :errChan)

      (expect #"sesame-seed\.server=>" 60000)
      (write-str "(run)\n")
      (expect #"Starting Figwheel")))
