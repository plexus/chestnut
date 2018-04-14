(ns {{project-ns}}.routes
  (:require [clojure.java.io :as io]
{{#compojure?}}
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
{{/compojure?}}
{{#bidi?}}
            [ring.util.response :refer [resource-response]]
            [bidi.bidi :as bidi]
            [bidi.ring :refer [resources]]
{{/bidi?}}
            [ring.util.response :refer [response]]))
{{#compojure?}}

(defn home-routes [endpoint]
  (routes
   (GET "/" _
     (-> "public/index.html"
         io/resource
         io/input-stream
         response
         (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
   (resources "/")))
{{/compojure?}}
{{#bidi?}}

(defn index-handler [req]
  (assoc (resource-response "index.html" {:root "public"})
         :headers {"Content-Type" "text/html; charset=UTF-8"}))

(def routes ["/" {""    {:get index-handler}
                  "css" {:get (resources {:prefix "public/css/"})}
                  "js"  {:get (resources {:prefix "public/js/"})}}])

(defn home-routes [endpoint]
  routes)
{{/bidi?}}
