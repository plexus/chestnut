(ns {{name}}.server
  (:require [cemerick.austin.repls :refer (browser-connected-repl-js)]
            [net.cgrand.enlive-html :as enlive]
            [compojure.route :refer (resources)]
            [compojure.core :refer (GET defroutes)]
            [ring.adapter.jetty :as jetty]
            [clojure.java.io :as io]
            [environ.core :refer [env]]))

(defn body-transforms []
  (if (env :is-dev)
    (comp
     (enlive/set-attr :class "is-dev")
     (enlive/prepend
      (enlive/html [:script {:type "text/javascript"} (browser-connected-repl-js)]))
     (enlive/prepend
      (enlive/html [:script {:type "text/javascript" :src "/out/goog/base.js"}]))
     (enlive/prepend
      (enlive/html [:script {:type "text/javascript" :src "/react/react.js"}]))
     (enlive/append
      (enlive/html [:script {:type "text/javascript"} "goog.require('{{name}}.core')"])))
    identity))

(enlive/deftemplate page (io/resource "index.html") [] [:body] (body-transforms))

(defn browser-repl []
  (let [repl-env (reset! cemerick.austin.repls/browser-repl-env
                         (cemerick.austin/repl-env))]
    (cemerick.austin.repls/cljs-repl repl-env)))

(defroutes site
  (resources "/")
  (resources "/react" {:root "react"})
  (GET "/*" req (page)))

(defn run [& [port]]
  (defonce ^:private server
    (jetty/run-jetty #'site {:port (Integer. (or port (env :port) 10555))
                             :join? false}))
  server)

(defn -main [& [port]]
  (run port))
