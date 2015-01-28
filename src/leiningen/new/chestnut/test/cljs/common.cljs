(ns foobar.test.common)

(defn insert-container! [container]
  (.appendChild container (.querySelector js/document "body")))


(defn new-container! []
  (let [id (str "container-" (gensym))
        n (.createElement js/document "DIV")]
    (set! (.-id n) id)
    (insert-container! n)
    (.querySelector js/document (str "#" id))))
