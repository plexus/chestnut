(ns leiningen.new.chestnut
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files
                                             sanitize sanitize-ns project-name]]
            [leiningen.core.main :as main]
            [clojure.string :refer [join]]))

(def render (renderer "chestnut"))

(defn wrap-indent [wrap n list]
  (fn []
    (->> list
         (map #(str "\n" (apply str (repeat n " ")) (wrap %)))
         (join ""))))

(defn dep-list [n list]
  (wrap-indent #(str "[" % "]") n list))

(defn indent [n list]
  (wrap-indent identity n list))

(defn http-kit? [opts]
  (some #{"--http-kit"} opts))

(defn site-middleware? [opts]
  (some #{"--site-middleware"} opts))

(defn om-tools? [opts]
  (some #{"--om-tools"} opts))

(defn cljx? [opts]
  (some #{"--cljx"} opts))

(defn less? [opts]
  (some #{"--less"} opts))

(defn sass? [opts]
  (some #{"--sass"} opts))

(defn spec? [opts]
  (some #{"--spec"} opts))

(def cljx-plugin
  "com.keminglabs/cljx \"0.4.0\" :exclusions [org.clojure/clojure]")

(def cljx-source-paths
  " \"target/generated/clj\" \"target/generated/cljx\"")

(defn server-clj-requires [opts]
  (if (http-kit? opts)
    ["org.httpkit.server :refer [run-server]"]
    ["ring.adapter.jetty :refer [run-jetty]"]))

(defn core-cljs-requires [opts]
  (if (om-tools? opts)
    ["om-tools.dom :as dom :include-macros true"
     "om-tools.core :refer-macros [defcomponent]"]
    ["om.dom :as dom :include-macros true"]))

(defn project-clj-deps [opts]
  (cond-> []
          (http-kit? opts) (conj "http-kit \"2.1.19\"")
          (om-tools? opts) (conj "prismatic/om-tools \"0.3.3\" :exclusions [org.clojure/clojure]")))

(defn project-plugins [opts]
  (cond-> []
          (cljx? opts) (conj cljx-plugin)))

(defn project-nrepl-middleware [opts]
  (cond-> []
          (cljx? opts) (conj "cljx.repl-middleware/wrap-cljx")))

(defn template-data [name opts]
  {:full-name name
   :name (project-name name)
   :project-goog-module (sanitize (sanitize-ns name))
   :project-ns (sanitize-ns name)
   :sanitized (name-to-path name)
   :server-clj-requires (dep-list 12 (server-clj-requires opts))
   :core-cljs-requires (dep-list 12 (core-cljs-requires opts))
   :project-clj-deps (dep-list 17 (project-clj-deps opts))
   :project-dev-plugins (dep-list 29 (project-plugins opts))
   :nrepl-middleware (indent 53 (project-nrepl-middleware opts))
   :server-command (if (http-kit? opts) "run-server" "run-jetty")
   :ring-defaults (if (site-middleware? opts) "site-defaults" "api-defaults")
   :not-om-tools? (fn [block] (if (om-tools? opts) "" block))

   ;; cljx
   :cljx-source-paths (if (cljx? opts) cljx-source-paths "")
   :cljx-extension (if (cljx? opts) "|\\.cljx")
   :cljx-cljsbuild-spath (if (cljx? opts) " \"target/generated/cljs\"" "")
   :cljx-hook? (fn [block] (if (cljx? opts) (str block "\n") ""))
   :cljx-build? (fn [block] (if (cljx? opts) (str block "\n") ""))
   :cljx-uberjar-hook (if (cljx? opts) "cljx.hooks " "")

   ;; tests
   :spec? (fn [block] (if (spec? opts) (str "\n" block) ""))
   :spec-plugin (if (spec? opts) "\n            [speclj \"3.1.0\"]" "")

   ;; sass stylesheets
   :sass? (fn [block] (if (sass? opts) (str "\n" block) ""))
   :sass-refer  (if (sass? opts) " start-sass" "")
   :sass-start  (if (sass? opts) "\n        (start-sass)" "")
   :sass-hook   (if (sass? opts) " leiningen.sassc" "")
   :sass-plugin (if (sass? opts) "\n            [org.clojars.aew/lein-sassc \"0.10.0\"]\n               [lein-auto \"0.1.1\"]" "")

   ;; less stylesheets
   :less? (fn [block] (if (less? opts) (str "\n" block) ""))
   :less-refer  (if (less? opts) " start-less" "")
   :less-start  (if (less? opts) "\n        (start-less)" "")
   :less-hook   (if (less? opts) " leiningen.less" "")
   :less-plugin (if (less? opts) "\n            [lein-less \"1.7.2\"]" "")})

(defn format-files-args [name opts]
  (let [data (template-data name opts)
        args [data
              ["project.clj"
               (render "project.clj" data)]
              ["resources/index.html"
               (render "resources/index.html" data)]
              (cond
               (less? opts) ["src/less/style.less"            (render "src/less/style.less" data)]
               (sass? opts) ["src/scss/style.scss"            (render "src/scss/style.scss" data)]
               :else        ["resources/public/css/style.css" (render "resources/public/css/style.css" data)])
              ["resources/public/js/polyfill.js"
               (render "resources/public/js/polyfill.js" data)]
              ["src/clj/{{sanitized}}/server.clj"
               (render "src/clj/chestnut/server.clj" data)]
              ["src/clj/{{sanitized}}/dev.clj"
               (render "src/clj/chestnut/dev.clj" data)]
              ["src/cljs/{{sanitized}}/core.cljs"
               (render "src/cljs/chestnut/core.cljs" data)]
              ["env/dev/cljs/{{sanitized}}/dev.cljs"
               (render "env/dev/cljs/chestnut/dev.cljs" data)]
              ["env/prod/cljs/{{sanitized}}/prod.cljs"
               (render "env/prod/cljs/chestnut/prod.cljs" data)]
              ["LICENSE"
               (render "LICENSE")]
              ["README.md"
               (render "README.md" data)]
              [".gitignore"
               (render ".gitignore")]

             ;; Heroku support
             ["system.properties"
              (render "system.properties")]
             ["Procfile"
              (render "Procfile" data)]]]

    (cond-> args
            (cljx? opts) (conj ["src/cljx/{{sanitized}}/core.cljx"
                                (render "src/cljx/chestnut/core.cljx" data)])
            (spec? opts) (conj ["bin/speclj"
                                (render "bin/speclj" data)]
                               ["spec/clj/{{sanitized}}/server_spec.clj"
                                (render "spec/clj/chestnut/server_spec.clj" data)]
                               ["spec/cljs/{{sanitized}}/core_spec.cljs"
                                (render "spec/cljs/chestnut/core_spec.cljs" data)]))))

(defn chestnut [name & opts]
  (main/info "Generating fresh 'lein new' chestnut project.")
  (apply ->files (format-files-args name opts)))
