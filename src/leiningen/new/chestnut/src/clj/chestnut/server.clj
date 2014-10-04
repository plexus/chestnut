(ns {{name}}.server
    (:require [clojure.java.io :as io]
              [{{name}}.dev :refer [is-dev? inject-devmode-html browser-repl start-figwheel]]
              [compojure.core :refer [GET defroutes]]
              [compojure.route :refer [resources]]
              [compojure.handler :refer [{{compojure-handler}}]]
              [net.cgrand.enlive-html :refer [deftemplate]]
              [ring.middleware.reload :as reload]
              [environ.core :refer [env]]{{{server-clj-requires}}}))

(deftemplate page
  (io/resource "index.html") [] [:body] (if is-dev? inject-devmode-html identity))

(defroutes routes
  (resources "/")
  (resources "/react" {:root "react"})
  (GET "/*" req (page)))

(def http-handler
  (if is-dev?
    (reload/wrap-reload ({{compojure-handler}} #'routes))
    ({{compojure-handler}} routes)))

(defn run [& [port]]
  (defonce ^:private server
    (do
      (if is-dev? (start-figwheel))
      (let [port (Integer. (or port (env :port) 10555))]
        (print "Starting web server on port" port ".\n")
        ({{server-command}} http-handler {:port port
                          :join? false}))))
  server)

(defn -main [& [port]]
  (run port))
