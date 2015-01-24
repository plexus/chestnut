(ns chestnut.test.integration
  (:require [mistletoe.process :refer :all]))

  (defn rm-rf [fname]
    (let [func (fn [func f]
                 (when (.isDirectory f)
                   (doseq [f2 (.listFiles f)]
                     (func func f2)))
                 (io/delete-file f))]
      (func func (io/file fname))))
