(ns {{name}}.server
  (:require [cemerick.austin.repls :refer (browser-connected-repl-js)]
            [net.cgrand.enlive-html :as enlive]
            [compojure.route :refer (resources)]
            [compojure.core :refer (GET defroutes)]
            [ring.adapter.jetty :as jetty]
            [clojure.java.io :as io]
            [environ.core :refer [env]]))

(defmacro if-dev-mode [then-body else-body]
  (if (not (env :production)) then-body else-body))

(if-dev-mode
 (enlive/deftemplate page (io/resource "index.html") []
   [:body] (enlive/append (enlive/html [:script (browser-connected-repl-js)])))
 (enlive/deftemplate page (io/resource "index.html") []))

(defn browser-repl []
  (let [repl-env (reset! cemerick.austin.repls/browser-repl-env
                         (cemerick.austin/repl-env))]
    (cemerick.austin.repls/cljs-repl repl-env)))

(defroutes site
  (resources "/")
  (GET "/*" req (page)))

(defn run [& [port]]
  (defonce ^:private server
    (jetty/run-jetty #'site {:port (Integer. (or port (env :port) 10555))
                             :join? false}))
  server)

(defn -main [& [port]]
  (run port))
