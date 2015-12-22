(ns {{project-ns}}.server
  (:require [clojure.java.io :as io]
            [{{project-ns}}.server-impl :as impl :refer [is-dev?]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [net.cgrand.enlive-html :refer [deftemplate]]
            [net.cgrand.reload :refer [auto-reload]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer [wrap-defaults {{ring-defaults}}]]
            [ring.middleware.browser-caching :refer [wrap-browser-caching]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [environ.core :refer [env]]{{{server-clj-requires}}})
  (:gen-class))

(deftemplate page (io/resource "index.html") []
  [:body] impl/inject-devmode-html)

(defroutes routes
  (resources "/")
  (resources "/react" {:root "react"})
  (GET "/*" req (page)))

(defn- wrap-browser-caching-opts [handler]
  (wrap-browser-caching handler (or (env :browser-caching) {})))

(def http-handler
  (cond-> routes
    true (wrap-defaults {{ring-defaults}})
    is-dev? reload/wrap-reload
    true wrap-browser-caching-opts
    true wrap-gzip))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (println (format "Starting web server on port %d." port))
    ({{server-command}} http-handler {:port port :join? false})))

(def run impl/run)
(def browser-repl impl/browser-repl)
(def -main run-web-server)
