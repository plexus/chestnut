(ns {{project-ns}}.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults {{ring-defaults}}]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [environ.core :refer [env]]
            [com.stuartsierra.component :as component]
            [reloaded.repl]
            (system.components
             [endpoint :refer [new-endpoint]]
             [handler :refer [new-handler]]
             [middleware :refer [new-middleware]]){{{server-clj-requires}}})
  (:gen-class))

(defroutes routes
  (GET "/" _
    {:status 200
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body (io/input-stream (io/resource "public/index.html"))})
  (resources "/"))

(defn get-http-handler [_]
  (-> routes))

(defn prod-system []
  (component/system-map
   :routes (new-endpoint get-http-handler)
   :middleware (new-middleware  {:middleware [[wrap-defaults :defaults]
                                              wrap-with-logger
                                              wrap-gzip]
                                 :defaults {{ring-defaults}}})
   :handler (component/using
             (new-handler)
             [:routes :middleware])
   :http (component/using
          (new-web-server (Integer. (or (env :port) 10555)))
          [:handler])))

(defn run-prod []
  (reloaded.repl/set-init! prod-system)
  (reloaded.repl/go))

(defn -main [& [port]]
  (run-prod))
