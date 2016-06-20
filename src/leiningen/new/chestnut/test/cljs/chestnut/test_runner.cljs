(ns {{project-ns}}.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [{{project-ns}}.core-test]{{#cljc?}}
   [{{project-ns}}.common-test]{{/cljc?}}))

(enable-console-print!)

(doo-tests '{{project-ns}}.core-test{{#cljc?}}
           '{{project-ns}}.common-test{{/cljc?}})
