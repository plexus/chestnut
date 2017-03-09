(ns {{project-ns}}.components.render
    (:require [com.stuartsierra.component :as component]
              [{{project-ns}}.core :refer [render]]))

(defrecord RenderComponent []
  component/Lifecycle
  (start [component]
    (render)
    component)
  (stop [component]
    component))
(defn new-render-component []
  (map->RenderComponent {}))
