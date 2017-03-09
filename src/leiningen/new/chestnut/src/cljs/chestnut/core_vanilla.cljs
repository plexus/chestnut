(ns {{project-ns}}.core)

(enable-console-print!)

(defn render []
  (set! (.-innerHTML (js/document.getElementById "app"))
        "<h1>Hello Chestnut!</h1>"))
