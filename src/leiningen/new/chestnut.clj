(ns leiningen.new.chestnut
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files
                                             sanitize sanitize-ns project-name]]
            [leiningen.core.main :as main]
            [clojure.string :as s]
            [clojure.java.io :as io]
            [clj-jgit.porcelain :refer :all]))

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
  ["http-kit" "site-middleware" "less" "sass" "reagent" "vanilla"])

(doseq [opt valid-options]
  (eval
   `(defn ~(symbol (str opt "?")) [opts#]
     (some #{~(str "--" opt)} opts#))))

(defn om? [props]
  (and (not (reagent? props)) (not (vanilla? props))))

(defn server-clj-requires [opts]
  (if (http-kit? opts)
    ["org.httpkit.server :refer [run-server]"]
    ["ring.adapter.jetty :refer [run-jetty]"]))

(defn project-clj-deps [opts]
  (cond-> []
    (http-kit? opts) (conj "http-kit \"2.1.19\"")
    (reagent? opts)  (conj "reagent \"0.5.1\"")
    (om? opts)       (conj "org.omcljs/om \"1.0.0-alpha35\"")))

(defn project-plugins [opts]
  (cond-> []
          (sass? opts) (conj "lein-sassc \"0.10.4\""
                             "lein-auto \"0.1.2\"")
          (less? opts) (conj "lein-less \"1.7.5\"")))

(defn project-uberjar-hooks [opts]
  (cond-> ["leiningen.cljsbuild"]
          (less? opts) (conj "leiningen.less")
          (sass? opts) (conj "leiningen.sassc")))

(defn load-props [file-name]
  (with-open [^java.io.Reader reader (clojure.java.io/reader file-name)]
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) v])))))

(defn chestnut-version []
  (let [resource (io/resource "META-INF/maven/chestnut/lein-template/pom.properties")
        props (load-props resource)
        version (:version props)
        revision (:revision props)
        snapshot? (re-find #"SNAPSHOT" version)]
    (str version " (" (s/join (take 8 revision)) ")")))

(defn template-data [name opts]
  {:full-name name
   :name                 (project-name name)
   :chestnut-version     (chestnut-version)
   :project-ns           (sanitize-ns name)
   :sanitized            (name-to-path name)
   :server-clj-requires  (dep-list 12 (server-clj-requires opts))

   :project-clj-deps     (dep-list 17 (project-clj-deps opts))
   :project-plugins      (dep-list 12 (project-plugins opts))
   :project-uberjar-hooks (s/join " " (project-uberjar-hooks opts))

   :server-command       (if (http-kit? opts) "run-server" "run-jetty")
   :ring-defaults        (if (site-middleware? opts) "site-defaults" "api-defaults")

   ;; features
   :sass?                (fn [block] (if (sass? opts) (str "\n" block) ""))
   :less?                (fn [block] (if (less? opts) (str "\n" block) ""))

   ;; stylesheets
   :less-sass-refer      (cond (sass? opts) " start-sass"
                               (less? opts) " start-less")
   :less-sass-start      (cond (sass? opts) "\n  (start-sass)"
                               (less? opts) "\n  (start-less)")})

(defn files-to-render [opts]
  (cond-> ["project.clj"
           "resources/public/index.html"
           "resources/log4j.properties"
           "src/clj/chestnut/server.clj"
           "dev/user.clj"
           "LICENSE"
           "README.md"
           "code_of_conduct.md"
           ".gitignore"
           "system.properties"
           "Procfile"
           "test/clj/chestnut/example_test.clj"
           "test/cljs/chestnut/core_test.cljs"
           "test/cljs/chestnut/test_runner.cljs"]
          (less? opts) (conj "src/less/style.less")
          (sass? opts) (conj "src/scss/style.scss")
          (not (or (less? opts) (sass? opts))) (conj "resources/public/css/style.css")))

(defn format-files-args
  "Returns a list of pairs (vectors). The first element is the file name to
render, the second is the file contents."
  [{:keys [name] :as data} opts]
  (letfn [(render-file [file]
            [(s/replace file "chestnut" "{{sanitized}}")
             (render file data)])]
    (conj
     (map render-file (files-to-render opts))
     ["src/cljs/{{sanitized}}/core.cljs"
      (render (cond
                (om? opts) "src/cljs/chestnut/core_om.cljs"
                (reagent? opts) "src/cljs/chestnut/core_reagent.cljs"
                (vanilla? opts) "src/cljs/chestnut/core_vanilla.cljs")
              data)])))

(defn chestnut [name & opts]
  (let [valid-opts (map (partial str "--") valid-options)]
    (doseq [opt opts]
      (if (not (some #{opt} valid-opts))
        (apply main/abort "Unrecognized option:" opt ". Should be one of" valid-opts))))
  (main/info "Generating fresh Chestnut project.")
  (main/info "README.md contains instructions to get you started.")

  (when (sass? opts)
    (main/info "WARNING: You have enabled SASS support, which relies on the sassc binary")
    (main/info "WARNING: being available on your system."))

  (let [data (template-data name opts)]
    (apply ->files data (format-files-args data opts)))

  (git-init name)
  (let [repo (load-repo name)]
    (git-add repo ".")
    (git-commit repo (str "lein new chestnut " name (s/join " " opts)))))
