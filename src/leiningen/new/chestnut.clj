(ns leiningen.new.chestnut
  (:require [chestnut.httpclient :as http]
            [clj-jgit.porcelain :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [leiningen.core.main :as main]
            [leiningen.new.templates :refer [->files name-to-path project-name
                                             render-text renderer sanitize-ns slurp-resource]]
            [ancient-clj.core :as ancient])
  (:import java.io.Writer))

(def default-project-deps '[[org.clojure/clojure "1.8.0"]
                            [org.clojure/clojurescript "1.9.854" :scope "provided"]
                            [com.cognitect/transit-clj "0.8.300"]
                            [ring "1.6.2"]
                            [ring/ring-defaults "0.3.1"]
                            [bk/ring-gzip "0.2.1"]
                            [radicalzephyr/ring.middleware.logger "0.6.0"]
                            [clj-logging-config "1.9.12"]
                            [compojure "1.6.0"]
                            [environ "1.1.0"]
                            [com.stuartsierra/component "0.3.2"]
                            [org.danielsz/system "0.4.0"]
                            [org.clojure/tools.namespace "0.2.11"]])

(def optional-project-deps '{:garden [lambdaisland/garden-watcher "0.3.1"]
                             :http-kit [http-kit "2.2.0"]
                             :lein-auto [lein-auto "0.1.3"]
                             :lein-less [lein-less "1.7.5"]
                             :lein-sassc [lein-sassc "0.10.4"]
                             :om [org.omcljs/om "1.0.0-beta1"]
                             :om-next [org.omcljs/om "1.0.0-beta1"]
                             :re-frame [re-frame "0.9.4"]
                             :reagent [reagent "0.7.0"]
                             :rum [rum "0.10.8"]})

(def default-project-plugins '[[lein-cljsbuild "1.1.7"]
                               [lein-environ "1.1.0"]])

(def project-clj-dev-deps '[[figwheel "0.5.12"]
                            [figwheel-sidecar "0.5.12"]
                            [com.cemerick/piggieback "0.2.2"]
                            [org.clojure/tools.nrepl "0.2.13"]
                            [lein-doo "0.1.7"]
                            [reloaded.repl "0.2.3"]])

(def project-clj-dev-plugins '[[lein-figwheel "0.5.12"]
                               [lein-doo "0.1.7"]])

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

(def options-descriptions
  {"help"            "Show this help information and exit."
   "http-kit"        "Use the http-kit web server, instead of Jetty."
   "site-middleware" "Use Ring's site-middleware, instead of the api-middleware. (session support)"
   "less"            "Use the LESS CSS pre-processor."
   "sass"            "Use the SASS CSS pre-processor."
   "reagent"         "Use Reagent as UI library."
   "vanilla"         "Don't use a UI library (vanilla Javascript)"
   "garden"          "Use Garden for writing CSS in Clojure"
   "rum"             "Use Rum as UI library"
   "om-next"         "Use om-next as UI library"
   "re-frame"        "Use re-frame as UI framework"
   "code-of-conduct" "Add a Code of Conduct to the project (Contributor Covenant)"
   "coc"             "Add a Code of Conduct to the project (Contributor Covenant)"
   "no-poll"         "Don't submit usage information"
   "edge"            "Automatically upgrade all libraries to the latest non-SNAPSHOT version."
   "bleeding-edge"   "Automatically upgrade all libraries to the latest non versions, inclusing SNAPSHOT releases."})

(def all-options
  ["http-kit"
   "site-middleware"
   "less"
   "sass"
   "garden"
   "reagent"
   "re-frame"
   "rum"
   "om-next"
   "vanilla"
   "code-of-conduct"
   "coc"
   "edge"
   "bleeding-edge"
   "no-poll"
   "help"])

(doseq [opt all-options]
  (eval
   `(defn ~(symbol (str opt "?")) [opts#]
      (some #{~(str "--" opt) ~(str "+" opt)} opts#))))

(defn om? [opts]
  (and (not (reagent? opts))
       (not (rum? opts))
       (not (vanilla? opts))
       (not (om-next? opts))
       (not (re-frame? opts))))

(defn some-edge? [opts]
  (or (edge? opts) (bleeding-edge? opts)))

(defn server-clj-requires [opts]
  (if (http-kit? opts)
    ["system.components.http-kit :refer [new-web-server]"]
    ["system.components.jetty :refer [new-web-server]"]))

(defn user-clj-requires [name opts]
  (cond-> []
    (or (sass? opts) (less? opts))
    (conj [(symbol (str (sanitize-ns name) ".components.shell-component :refer [shell-component]"))])

    (garden? opts)
    (conj '[garden-watcher.core :refer [new-garden-watcher]])))

(defn update-dep [dep opts]
  (cond-> dep
    (some-edge? opts)
    (assoc 1 (ancient/latest-version-string! dep {:snapshots? (bleeding-edge? opts)}))))

(defn update-deps [deps opts]
  (map #(update-dep % opts) deps))

(defn project-clj-deps [opts]
  (update-deps
   (cond-> default-project-deps
     (http-kit? opts) (conj (get optional-project-deps :http-kit))
     (reagent? opts)  (conj (get optional-project-deps :reagent))
     (om? opts)       (conj (get optional-project-deps :om))
     (om-next? opts)  (conj (get optional-project-deps :om-next))
     (rum? opts)      (conj (get optional-project-deps :rum))
     (re-frame? opts) (conj (get optional-project-deps :re-frame))
     (garden? opts)   (conj (get optional-project-deps :garden)))
   opts))

(defn project-plugins [opts]
  (update-deps
   (cond-> default-project-plugins
     (sass? opts) (conj (get optional-project-deps :lein-sassc)
                        (get optional-project-deps :lein-auto))
     (less? opts) (conj (get optional-project-deps :lein-less)))
   opts))

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
    (into [:garden-watcher (list 'new-garden-watcher [(list 'quote (symbol (str (sanitize-ns name) ".styles")))])])

    (less? opts)
    (into [:less (list 'shell-component "lein" "less" "auto")])

    (sass? opts)
    (into [:sass (list 'shell-component "lein" "auto" "sassc" "once")])))

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
  {:full-name            name
   :name                 (project-name name)
   :chestnut-version     (chestnut-version)
   :project-ns           (sanitize-ns name)
   :ns-name              (sanitize-ns name) ;; used by re-frame template
   :sanitized            (name-to-path name)

   :user-clj-requires    (indent 12 (map pr-str (user-clj-requires name opts)))
   :server-clj-requires  (dep-list 12 (server-clj-requires opts))

   :project-clj-deps     (indent-next 17 (map pr-str (project-clj-deps opts)))
   :project-plugins      (indent-next 12 (map pr-str (project-plugins opts)))
   :project-clj-dev-deps (indent-next 29 (map pr-str (update-deps project-clj-dev-deps opts)))
   :project-clj-dev-plugins (indent-next 24 (map pr-str (update-deps project-clj-dev-plugins opts)))
   :project-prep-tasks   (indent-next 27 (map pr-str (project-prep-tasks name opts)))
   :project-uberjar-hooks (str/join " " (project-uberjar-hooks opts))

   :server-command       (if (http-kit? opts) "run-server" "run-jetty")
   :ring-defaults        (if (site-middleware? opts) "site-defaults" "api-defaults")

   ;; TODO: get rid of these, instead use something like :project-clj-extras
   :less?                (less? opts)
   :sass?                (sass? opts)

   :extra-dev-components (indent 4 (->> (extra-dev-components name opts)
                                        (map pr-str)
                                        (partition 2)
                                        (map #(str/join " " %))))})

(defn files-to-render [opts]
  (cond-> ["project.clj"
           "resources/public/index.html"
           "resources/log4j.properties"
           "src/clj/chestnut/application.clj"
           "src/clj/chestnut/routes.clj"
           "src/clj/chestnut/config.clj"
           "src/clj/chestnut/components/server_info.clj"
           "src/cljc/chestnut/common.cljc"
           "src/cljs/chestnut/system.cljs"
           "src/cljs/chestnut/components/ui.cljs"
           "dev/user.clj"
           "dev/cljs/user.cljs"
           "LICENSE"
           "README.md"
           "Procfile"
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
    (or (less? opts) (sass? opts)) (conj "src/clj/chestnut/components/shell_component.clj")
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

(defn do-pop-poll [version flags]
  (try
    (http/http-post! "https://lambdaisland.com/chestnut-poll" {:version version, :flags (str/join " " flags)})
    (catch Throwable e)))

(defn print-help! []
  (main/info "Chestnut: friendly Clojure/ClojureScript application template")
  (main/info "")
  (main/info "Usage: lein new chestnut <app-name> <options>")
  (main/info " e.g.: lein new chestnut dating-for-hamsters +http-kit +re-frame +code-of-conduct")
  (main/info "")
  (doseq [opt all-options]
    (main/info (format "  +%-20s %s" opt (options-descriptions opt))))
  (main/info "")
  (main/info "Please consult the Chestnut README (https://github.com/plexus/chestnut) as well as the README in the generated project."))

(defn chestnut [name & opts]
  (when (some #{"+help" "--help"} (cons name opts))
    (print-help!)
    (System/exit 0))

  (let [dash-opts (map (partial str "--") all-options)
        plus-opts (map (partial str "+") all-options)]
    (doseq [opt opts]
      (when (not (some #{opt} (concat dash-opts plus-opts)))
        (print-help!)
        (apply main/abort "Unrecognized option:" opt ". Should be one of" plus-opts))))

  (main/info "Generating fresh Chestnut project.")
  (main/info "README.md contains instructions to get you started.")

  (let [data (template-data name opts)
        files (cond-> (format-files-args data opts)
                (re-frame? opts) (concat (re-frame-files data)))]
    (apply ->files data files))

  (when-not (no-poll? opts)
    (do-pop-poll (chestnut-version) (map #(str/replace % #"^[-\+]+" "") opts)))

  (git-init name)
  (let [repo (load-repo name)]
    (git-add repo ".")
    (git-commit repo (str "lein new chestnut " name " " (str/join " " opts)))))
