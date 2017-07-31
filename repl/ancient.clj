(ns repl.ancient)

(require '[ancient-clj.core :as ancient]
         '[leiningen.new.chestnut :as chestnut])

ancient/default-repositories

(ancient/latest-version-string! 'ancient-clj)

(defn update-dep [dep]
  (assoc dep 1 (ancient/latest-version-string! dep {:snapshots? false})))

(let [deps (map update-dep chestnut/default-project-deps)
      plugins (map update-dep chestnut/default-project-plugins)
      dev-deps (map update-dep chestnut/project-clj-dev-deps)
      dev-plugins (map update-dep chestnut/project-clj-dev-plugins)
      optional-deps (reduce #(update %1 %2 update-dep)
                            chestnut/optional-project-deps
                            (keys chestnut/optional-project-deps))]

  (print
   (str "(def default-project-deps '["
        (chestnut/indent-next 28 (map pr-str deps))
        "])"
        "\n\n"
        "(def optional-project-deps '{"
        (chestnut/indent-next 29 (map #(str (pr-str (first %)) " " (pr-str (second %))) optional-deps))
        "})"
        "\n\n"
        "(def default-project-plugins '["
        (chestnut/indent-next 31 (map pr-str plugins))
        "])"
        "\n\n"
        "(def project-clj-dev-deps '["
        (chestnut/indent-next 28 (map pr-str dev-deps))
        "])"
        "\n\n"
        "(def project-clj-dev-plugins '["
        (chestnut/indent-next 31 (map pr-str dev-plugins))
        "])"
        ))
  )

(def optional-project-deps '{:http-kit   [http-kit "2.2.0"]
                             :reagent    [reagent "0.6.0"]
                             :om         [org.omcljs/om "1.0.0-alpha48"]
                             :om-next    [org.omcljs/om "1.0.0-alpha48"]
                             :rum        [rum "0.10.8"]
                             :re-frame   [re-frame "0.9.4"]
                             :garden     [lambdaisland/garden-watcher "0.3.1"]
                             :lein-sassc [lein-sassc "0.10.4"]
                             :lein-auto  [lein-auto "0.1.3"]
                             :lein-less  [lein-less "1.7.5"]})
