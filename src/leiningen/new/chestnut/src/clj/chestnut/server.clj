(ns {{project-ns}}.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults {{ring-defaults}}]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [environ.core :refer [env]]{{{server-clj-requires}}})
  (:gen-class))

(defroutes routes
  (resources "/"))

(def http-handler
  (-> routes
      (wrap-defaults {{ring-defaults}})
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    ({{server-command}} http-handler {:port port :join? false})))
