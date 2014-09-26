(ns {{name}}.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [site]]
            [net.cgrand.enlive-html :as html :refer [deftemplate]]
            [environ.core :refer [env]]
            [cemerick.piggieback :as piggieback]
            [weasel.repl.websocket :as weasel]
            [ring.middleware.reload :as reload]
            {{{server-clj-requires}}}))

(def is-dev? (env :is-dev))

(defn body-transforms []
  (if is-dev?
    (comp
     (html/set-attr :class "is-dev")
     (html/prepend (html/html [:script {:type "text/javascript" :src "/out/goog/base.js"}]))
     (html/prepend (html/html [:script {:type "text/javascript" :src "/react/react.js"}]))
     (html/append  (html/html [:script {:type "text/javascript"} "goog.require('{{sanitized}}.core')"])))
    identity))

(deftemplate page
  (io/resource "index.html") [] [:body] (body-transforms))

(defroutes routes
  (resources "/")
  (resources "/react" {:root "react"})
  (GET "/*" req (page)))

(defn browser-repl []
  (piggieback/cljs-repl :repl-env (weasel/repl-env :ip "0.0.0.0" :port 9001)))

(defn run [& [port]]
  (defonce ^:private server
    (let [handler (if is-dev?
                    (reload/wrap-reload (site #'routes))
                    (site routes))]
      ({{server-command}} handler {:port (Integer. (or port (env :port) 10555))
                                  :join? false})))
  server)

(defn -main [& [port]]
  (run port))
