(ns {{project-ns}}.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [{{project-ns}}.core-test]))

(enable-console-print!)

(doo-tests '{{project-ns}}.core-test)
