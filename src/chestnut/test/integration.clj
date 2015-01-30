(ns chestnut.test.integration
  (:require [clojure.java.io :as io]
            [mistletoe.process :refer :all]
            [mistletoe.test :refer :all]
            [clj-webdriver.taxi :as browser]
            [environ.core :refer [env]]))

(def browser-type :chrome)

;; The path to the driver executable must be set by the
;; webdriver.chrome.driver system property; for more information, see
;; http://code.google.com/p/selenium/wiki/ChromeDriver. The latest
;; version can be downloaded from
;; http://chromedriver.storage.googleapis.com/index.html
(System/setProperty "webdriver.chrome.driver" (str (env :home) "/bin/chromedriver"))

(defn rm-rf [fname]
  (if (= fname "/")
    (throw (Exception. "Refusing to 'rm -rf /'")))
  (let [func (fn [func f]
               (when (.isDirectory f)
                 (doseq [f2 (.listFiles f)]
                   (func func f2)))
               (when (.exists f)
                 (io/delete-file f)))]
    (func func (io/file fname))))

(defn spawn [name dir cmd]
  (-> (process "sh" "-c" (str "cd " dir ";" cmd))
      (start)
      (start-pipe :in)
      (start-pipe :err)
      (log-chan (str name "       | "))
      (log-chan (str name " (err) | ") :errChan)))

(def generate-new-app []
  (println "--> Generating app in /tmp/sesame-seed")
  (rm-rf "/tmp/sesame-seed")
  (waitFor (spawn "LEIN NEW" "/tmp" "lein new chestnut sesame-seed --snapshot")))

(defn spawn-repl []
  (println "--> Starting REPL")
  (-> (spawn "REPL" "/tmp/sesame-seed" "lein repl")
      (expect #"sesame-seed\.server=>" 60000)))

(defn test-basic []
  (generate-new-app)
  (let [repl (spawn-repl)]
    (write-str repl "(run)\n")
    (expect repl #"notifying browser that file changed")

    (browser/set-driver! {:browser browser-type} "http://localhost:10555")

    (write-str repl "(quit)\n")

    ))

(defn -main []
  (test-basic))
