(ns leiningen.new.chestnut
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "chestnut"))

(defn chestnut [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' chestnut project.")
    (->files data
             ["project.clj"
              (render "project.clj" data)]
             ["resources/index.html"
              (render "resources/index.html" data)]
             ["src/clj/{{sanitized}}/server.clj"
              (render "src/clj/chestnut/server.clj" data)]
             ["src/cljs/{{sanitized}}/core.cljs"
              (render "src/cljs/chestnut/core.cljs" data)]
             ["LICENSE"
              (render "LICENSE" data)]
             ["README.md"
              (render "README.md" data)]
             [".gitignore"
              (render ".gitignore" data)]

             ;; Heroku support
             ["system.properties"
              (render "system.properties" data)]
             ["Procfile"
              (render "Procfile" data)])))
