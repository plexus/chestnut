(ns {{project-ns}}.server
  (:require [clojure.java.io :as io]
            [{{project-ns}}.dev :refer [is-dev? inject-devmode-html browser-repl start-figwheel{{less-sass-refer}}]]
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
  [:body] (if is-dev? inject-devmode-html identity))

(defroutes routes
  (resources "/")
  (resources "/react" {:root "react"})
  (GET "/*" req (page)))

(defn- wrap-browser-caching-opts [handler] (wrap-browser-caching handler (or (env :browser-caching) {})))

(def http-handler
    (-> routes
        (wrap-defaults {{ring-defaults}})
        (if is-dev? reload/wrap-reload identity)
        wrap-browser-caching-opts
        wrap-gzip))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (println (format "Starting web server on port %d." port))
    ({{server-command}} http-handler {:port port :join? false})))

(defn run-auto-reload [& [port]]
  (auto-reload *ns*)
  (start-figwheel){{less-sass-start}})

(defn run [& [port]]
  (when is-dev?
    (run-auto-reload))
  (run-web-server port))

(defn -main [& [port]]
  (run port))

