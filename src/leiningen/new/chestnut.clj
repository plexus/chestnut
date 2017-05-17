(ns leiningen.new.chestnut
  (:require [clj-jgit.porcelain :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [leiningen.core.main :as main]
            [leiningen.new.templates :refer [->files name-to-path project-name render-text renderer sanitize-ns slurp-resource]])
  (:import java.io.Writer))

;; When using `pr`, output quoted forms as 'foo, and not as (quote foo)
(defmethod clojure.core/print-method clojure.lang.ISeq [o ^Writer w]
  (#'clojure.core/print-meta o w)
  (if (= (first o) 'quote)
    (do
      (.write w "'")
      (print-method (second o) w))
    (#'clojure.core/print-sequential "(" #'clojure.core/pr-on " " ")" o w)))

(def render (renderer "chestnut"))

(defn wrap-indent [wrap n list]
  (fn []
    (->> list
         (map #(str "\n" (apply str (repeat n " ")) (wrap %)))
         (str/join ""))))

(defn dep-list [n list]
  (wrap-indent #(str "[" % "]") n list))

(defn indent [n list]
  (wrap-indent identity n list))

(defn indent-next [n list]
  (str (first list)
       ((indent n (next list)))))

(def valid-options
  ["http-kit" "site-middleware" "less" "sass" "reagent" "vanilla" "garden" "rum" "om-next" "re-frame" "code-of-conduct" "coc"])

(doseq [opt valid-options]
  (eval
   `(defn ~(symbol (str opt "?")) [opts#]
      (some #{~(str "--" opt) ~(str "+" opt)} opts#))))

(defn om? [props]
  (and (not (reagent? props))
       (not (rum? props))
       (not (vanilla? props))
       (not (om-next? props))
       (not (re-frame? props))))

(defn server-clj-requires [opts]
  (if (http-kit? opts)
    ["system.components.http-kit :refer [new-web-server]"]
    ["system.components.jetty :refer [new-web-server]"]))

(defn user-clj-requires [opts]
  (cond-> []
    (or (sass? opts) (less? opts)) (conj '[clojure.java.io :as io])
    (garden? opts)                 (conj '[garden-watcher.core :refer [new-garden-watcher]])))

(defn project-clj-deps [opts]
  (cond-> []
    (http-kit? opts) (conj '[http-kit "2.2.0"])
    (reagent? opts)  (conj '[reagent "0.6.0"])
    (om? opts)       (conj '[org.omcljs/om "1.0.0-alpha48"])
    (om-next? opts)  (conj '[org.omcljs/om "1.0.0-alpha48"])
    (rum? opts)      (conj '[rum "0.10.8"])
    (re-frame? opts) (conj '[re-frame "0.9.2"])
    (garden? opts)   (conj '[lambdaisland/garden-watcher "0.3.1"])))

(defn project-plugins [opts]
  (cond-> []
    (sass? opts) (conj "lein-sassc \"0.10.4\""
                       "lein-auto \"0.1.3\"")
    (less? opts) (conj "lein-less \"1.7.5\"")))

(defn project-prep-tasks [name opts]
  (cond-> ["compile" ["cljsbuild" "once" "min"]]
    (garden? opts) (conj ["run" "-m" "garden-watcher.main" (str (sanitize-ns name) ".styles")])))

(defn project-uberjar-hooks [opts]
  (cond-> []
    (less? opts) (conj "leiningen.less")
    (sass? opts) (conj "leiningen.sassc")))

(defn extra-dev-components [name opts]
  (cond-> []
    (garden? opts)
    (into [:garden-watcher (list 'new-garden-watcher [(list 'quote (symbol (str (sanitize-ns name) ".styles")))])])))

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
    (str version " (" (str/join (take 8 revision)) ")")))

(defn template-data [name opts]
  {:full-name name
   :name                 (project-name name)
   :chestnut-version     (chestnut-version)
   :project-ns           (sanitize-ns name)
   :sanitized            (name-to-path name)

   :user-clj-requires    (indent 12 (map pr-str (user-clj-requires opts)))
   :server-clj-requires  (dep-list 12 (server-clj-requires opts))

   :project-clj-deps     (indent 17 (map pr-str (project-clj-deps opts)))
   :project-plugins      (dep-list 12 (project-plugins opts))
   :project-prep-tasks   (indent-next 27 (map pr-str (project-prep-tasks name opts)))
   :project-uberjar-hooks (str/join " " (project-uberjar-hooks opts))

   :server-command       (if (http-kit? opts) "run-server" "run-jetty")
   :ring-defaults        (if (site-middleware? opts) "site-defaults" "api-defaults")

   :extra-dev-components (indent 4 (->> (extra-dev-components name opts)
                                        (map pr-str)
                                        (partition 2)
                                        (map #(str/join " " %))))

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
           "src/clj/chestnut/application.clj"
           "src/clj/chestnut/routes.clj"
           "src/cljc/chestnut/common.cljc"
           "src/cljs/chestnut/system.cljs"
           "src/cljs/chestnut/components/ui.cljs"
           "dev/user.clj"
           "dev/cljs/user.cljs"
           "LICENSE"
           "README.md"
           ".gitignore"
           "system.properties"
           ".dir-locals.el"
           "test/clj/chestnut/example_test.clj"
           "test/cljs/chestnut/core_test.cljs"
           "test/cljs/chestnut/test_runner.cljs"
           "test/cljc/chestnut/common_test.cljc"]
    (or (coc? opts) (code-of-conduct? opts)) (conj "code_of_conduct.md")
    (less? opts) (conj "src/less/style.less")
    (sass? opts) (conj "src/scss/style.scss")
    (garden? opts) (conj "src/clj/chestnut/styles.clj")
    (not (or (less? opts) (sass? opts))) (conj "resources/public/css/style.css")))

(defn format-files-args
  "Returns a list of pairs (vectors). The first element is the file name to
  render, the second is the file contents."
  [{:keys [name] :as data} opts]
  (letfn [(render-file [file]
            [(str/replace file "chestnut" "{{sanitized}}")
             (render file data)])]
    (conj
     (map render-file (files-to-render opts))
     ["src/cljs/{{sanitized}}/core.cljs"
      (render (cond
                (om? opts) "src/cljs/chestnut/core_om.cljs"
                (om-next? opts) "src/cljs/chestnut/core_om_next.cljs"
                (reagent? opts) "src/cljs/chestnut/core_reagent.cljs"
                (rum? opts) "src/cljs/chestnut/core_rum.cljs"
                (vanilla? opts) "src/cljs/chestnut/core_vanilla.cljs"
                (re-frame? opts) "src/cljs/chestnut/core_re_frame.cljs")
              data)])))

(defn re-frame-render
  [resource-path data]
  (-> (str "leiningen/new/re_frame/" resource-path)
      io/resource
      slurp-resource
      (render-text data)))

(defn re-frame-files [data]
  (for [f ["config" "db" "subs" "events" "views"]]
    [(str "src/cljs/{{sanitized}}/" f ".cljs")
     (re-frame-render (str "src/cljs/" f ".cljs") data)]))

(defn chestnut [name & opts]
  (let [dash-opts (map (partial str "--") valid-options)
        plus-opts (map (partial str "+") valid-options)]
    (doseq [opt opts]
      (if (not (some #{opt} (concat dash-opts plus-opts)))
        (apply main/abort "Unrecognized option:" opt ". Should be one of" plus-opts))))
  (main/info "Generating fresh Chestnut project.")
  (main/info "README.md contains instructions to get you started.")

  (when (sass? opts)
    (main/info "WARNING: You have enabled SASS support, which relies on the sassc binary")
    (main/info "WARNING: being available on your system."))

  (let [data (template-data name opts)]
    (apply ->files data (format-files-args data opts))

    (when (re-frame? opts)
      (apply ->files data (re-frame-files (assoc data :ns-name (:project-ns data))))))

  (git-init name)
  (let [repo (load-repo name)]
    (git-add repo ".")
    (git-commit repo (str "lein new chestnut " name " " (str/join " " opts)))))
