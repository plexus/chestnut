(ns {{project-ns}}.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response]]))

(defroutes routes
  (GET "/" _
    (-> "public/index.html"
        io/resource
        io/input-stream
        response
        (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
  (resources "/"))
