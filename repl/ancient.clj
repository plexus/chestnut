(ns repl.ancient
  (:require [ancient-clj.core :as ancient]
            [leiningen.new.chestnut :as chestnut]))

(comment
  ancient/default-repositories

  (ancient/latest-version-string! 'ancient-clj))

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
        (chestnut/indent-next 29 (sort (map #(str (pr-str (first %)) " " (pr-str (second %))) optional-deps)))
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
        "])")))
