(ns {{project-ns}}.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE routes context]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response]]))

(defn new-routes [endpoint]
  (routes
   (GET "/" _
     (-> "public/index.html"
         io/resource
         io/input-stream
         response
         (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
   (resources "/")))
