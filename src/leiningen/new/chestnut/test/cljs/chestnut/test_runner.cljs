(ns {{project-ns}}.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [{{project-ns}}.core-test]
   [{{project-ns}}.common-test]))

(enable-console-print!)

(doo-tests '{{project-ns}}.core-test
           '{{project-ns}}.common-test)
