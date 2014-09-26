(ns leiningen.new.chestnut
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]
            [clojure.string :refer [join]]))

(def render (renderer "chestnut"))

(defn dep-list [n list]
  (fn []
    (join "\n"
          (conj (map #(str (apply str (repeat n " "))  "[" % "]") (rest list))
                (str "[" (first list) "]")))))

(defn http-kit? [opts]
  (some #{"--http-kit"} opts))

(defn server-clj-requires [opts]
  (if (http-kit? opts)
    ["org.httpkit.server :refer [run-server]"]
    ["ring.adapter.jetty :refer [run-jetty]"]))

(defn project-clj-deps [opts]
  (if (http-kit? opts) ["http-kit \"2.1.19\""] []))

(defn chestnut [name & opts]
  (let [data {:name name
              :sanitized (name-to-path name)
              :server-clj-requires (dep-list 12 (server-clj-requires opts))
              :project-clj-deps (dep-list 17 (project-clj-deps opts))
              :server-command (if (http-kit? opts) "run-server" "run-jetty")
              }]
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
