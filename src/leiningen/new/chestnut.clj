(ns leiningen.new.chestnut
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files
                                             sanitize sanitize-ns project-name]]
            [leiningen.core.main :as main]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(def render (renderer "chestnut"))

(defn wrap-indent [wrap n list]
  (fn []
    (->> list
         (map #(str "\n" (apply str (repeat n " ")) (wrap %)))
         (s/join ""))))

(defn dep-list [n list]
  (wrap-indent #(str "[" % "]") n list))

(defn indent [n list]
  (wrap-indent identity n list))

(def valid-options
  ["http-kit" "site-middleware" "om-tools" "cljx" "less" "sass" "speclj"])

(doseq [opt valid-options]
  (eval
   `(defn ~(symbol (str opt "?")) [opts#]
     (some #{~(str "--" opt)} opts#))))

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
          (om-tools? opts) (conj "prismatic/om-tools \"0.3.9\" :exclusions [potemkin]")))

(defn project-dev-deps [opts]
  (cond-> []
          (speclj? opts) (conj "speclj \"3.1.0\"")))

(defn project-plugins [opts]
  (cond-> []
          (sass? opts) (conj "lein-sassc \"0.10.4\""
                             "lein-auto \"0.1.1\"")
          (less? opts) (conj "lein-less \"1.7.2\"")))

(defn project-uberjar-hooks [opts]
  (cond-> ["leiningen.cljsbuild"]
          (less? opts) (conj "leiningen.less")
          (sass? opts) (conj "leiningen.sassc")))

(defn project-dev-plugins [opts]
  (cond-> []
          (speclj? opts) (conj "speclj \"3.1.0\"")
          (cljx? opts) (conj "com.keminglabs/cljx \"0.5.0\" :exclusions [org.clojure/clojure]")))

(defn project-nrepl-middleware [opts]
  (cond-> []
          (cljx? opts) (conj "cljx.repl-middleware/wrap-cljx")))

(defn load-props [file-name]
  (with-open [^java.io.Reader reader (clojure.java.io/reader file-name)]
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) v])))))

(defn chestnut-version []
  (let [props (load-props (io/resource "META-INF/maven/chestnut/lein-template/pom.properties"))
        version (:version props)
        revision (:revision props)
        snapshot? (re-find #"SNAPSHOT" version)]

    (str version (if snapshot? (str " (" (s/join (take 8 revision)) ")")))))

(defn template-data [name opts]
  {:full-name name
   :name                 (project-name name)
   :chestnut-version     (chestnut-version)
   :project-goog-module  (sanitize (sanitize-ns name))
   :project-ns           (sanitize-ns name)
   :sanitized            (name-to-path name)
   :server-clj-requires  (dep-list 12 (server-clj-requires opts))
   :core-cljs-requires   (dep-list 12 (core-cljs-requires opts))

   :project-clj-deps     (dep-list 17 (project-clj-deps opts))
   :project-plugins      (dep-list 12 (project-plugins opts))
   :project-dev-plugins  (dep-list 29 (project-dev-plugins opts))
   :project-dev-deps     (dep-list 34 (project-dev-deps opts))
   :project-uberjar-hooks (s/join " " (project-uberjar-hooks opts))

   :nrepl-middleware     (indent 53 (project-nrepl-middleware opts))
   :server-command       (if (http-kit? opts) "run-server" "run-jetty")
   :ring-defaults        (if (site-middleware? opts) "site-defaults" "api-defaults")

   ;; features
   :not-om-tools?        (fn [block] (if (om-tools? opts) "" block))
   :sass?                (fn [block] (if (sass? opts) (str "\n" block) ""))
   :less?                (fn [block] (if (less? opts) (str "\n" block) ""))
   :cljx?                (fn [block] (if (cljx? opts) (str "\n" block) ""))

   ;; testing features
   :speclj?              (fn [block] (if (speclj? opts) (str "\n" block) ""))
   :test-src-path        (if (speclj? opts) "\"spec/cljs\"" "\"test/cljs\"")
   :test-command-name    (if (speclj? opts) "\"spec\"" "\"test\"")
   :test-command         (if (speclj? opts)
                           "[\"phantomjs\" \"bin/speclj\" \"resources/public/js/app_test.js\"]"
                           "[\"phantomjs\" \"env/test/js/unit-test.js\" \"env/test/unit-test.html\"]")


   ;; stylesheets
   :less-sass-refer      (cond (sass? opts) " start-sass"
                               (less? opts) " start-less")
   :less-sass-start      (cond (sass? opts) "\n  (start-sass)"
                               (less? opts) "\n  (start-less)")

   ;; cljx
   :project-source-paths (if (cljx? opts) " \"target/generated/clj\" \"target/generated/cljx\"" "")
   :cljx-extension       (if (cljx? opts) "|\\.cljx")
   :cljx-cljsbuild-spath (if (cljx? opts) " \"target/generated/cljs\"" "")})

(defn files-to-render [opts]
  (cond-> ["project.clj"
           "resources/index.html"
           "src/clj/chestnut/server.clj"
           "src/cljs/chestnut/core.cljs"
           "env/dev/clj/chestnut/dev.clj"
           "env/dev/cljs/chestnut/dev.cljs"
           "env/prod/clj/chestnut/dev.clj"
           "env/prod/cljs/chestnut/prod.cljs"
           "env/test/js/polyfill.js"
           "LICENSE"
           "README.md"
           "code_of_conduct.md"
           ".gitignore"
           "system.properties"
           "Procfile"]
          (less? opts) (conj "src/less/style.less")
          (sass? opts) (conj "src/scss/style.scss")
          (not (or (less? opts) (sass? opts))) (conj "resources/public/css/style.css")
          (cljx? opts) (conj "src/cljx/chestnut/core.cljx")
          (speclj? opts) (conj "bin/speclj"
                             "spec/clj/chestnut/server_spec.clj"
                             "spec/cljs/chestnut/core_spec.cljs")
          (not (speclj? opts)) (conj "env/test/js/unit-test.js"
                                     "env/test/unit-test.html"
                                     "test/clj/chestnut/example_test.clj"
                                     "test/cljs/chestnut/core.cljs"
                                     "test/cljs/chestnut/common.cljs"
                                     "test/cljs/chestnut/test-runner.cljs")))

(defn format-files-args [name opts]
  (let [data (template-data name opts)
        render-file (fn [file]
                      [(s/replace file "chestnut" "{{sanitized}}")
                       (render file data)])]
    (cons data (map render-file (files-to-render opts)))))

(defn chestnut [name & opts]
  (let [valid-opts (map (partial str "--") valid-options)]
    (doseq [opt opts]
      (if (not (some #{opt} valid-opts))
        (apply main/abort "Unrecognized option:" opt ". Should be one of" valid-opts))))
  (main/info "Generating fresh Chestnut project.")
  (main/info "README.md contains instructions to get you started.")

  (when (sass? opts)
    (main/info "WARNING: You have enabled SASS support, which relies on the sassc binary")
    (main/info "WARNING: being available on your system. This is an advanced, undocumented")
    (main/info "WARNING: feature. In other words: you're on your own."))

  (apply ->files (format-files-args name opts)))
