(ns {{project-ns}}.config
  (:require [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults {{ring-defaults}}]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]))

(defn config []
  {:http-port  (Integer. (or (env :port) 10555))
   :middleware [[wrap-defaults {{ring-defaults}}]
                wrap-with-logger
                wrap-gzip]})
