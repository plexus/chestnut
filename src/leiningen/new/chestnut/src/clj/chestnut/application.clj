(ns {{project-ns}}.application
  (:gen-class)
  (:require [ring.middleware.defaults :refer [wrap-defaults {{ring-defaults}}]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [environ.core :refer [env]]
            [com.stuartsierra.component :as component]
            [system.components.endpoint :refer [new-endpoint]]
            [system.components.handler :refer [new-handler]]
            [system.components.middleware :refer [new-middleware]]{{{server-clj-requires}}}
            [{{project-ns}}.routes :refer [new-routes]]))

(defn app-system []
  (component/system-map
   :routes (new-endpoint new-routes)
   :middleware (new-middleware {:middleware [[wrap-defaults {{ring-defaults}}]
                                             wrap-with-logger
                                             wrap-gzip]})
   :handler (-> (new-handler)
                (component/using [:routes :middleware]))
   :http (-> (new-web-server (Integer. (or (env :port) 10555)))
             (component/using [:handler]))))

(defn -main [& _]
  (component/start (app-system)))
