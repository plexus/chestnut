(ns {{name}}.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [{{name}}.dev :refer [is-dev?]]
            [cljs.core.async :refer [chan <!]]
            [om.core :as om :include-macros true]{{{core-cljs-requires}}}))

(defonce app-state (atom {:text "Hello Chestnut!"}))
(defonce re-render-ch (chan))

(om/root
  (fn [app owner]
    (reify
      om/IWillMount
      (will-mount [_]
        (go (loop []
              (when (<! re-render-ch)
                (om/refresh! owner)
                (recur)))))
      om/IRender
      (render [_]
        (dom/h1 {{#not-om-tools?}} nil{{/not-om-tools?}}(:text app)))))
  app-state
  {:target (. js/document (getElementById "app"))})
