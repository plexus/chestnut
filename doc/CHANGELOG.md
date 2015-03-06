# Changelog

### v0.7.0

* Added Clojure{,script} unit testing support with Phantom JS.
  ([Rory Gibson](http://github.com/rorygibson))
* Update om-tools: 0.3.9 => 0.3.10
* Update ring-defaults: 0.1.2 => 0.1.3
* Use figwheel-sidecar, drop the dependency on leiningen
* renamed the per-env namespace from dev to main
* give a proper error message when trying to invoke less in production

### v0.7.0-SNAPSHOT-20150103

* Validate command line arguments
* Mention Chestnut version in the generated README
* Fix om-tools support
* SASSC support has been fixed, but is considered undocumented and unsupported
* Update Om: 0.7.0 =>  0.8.0-rc1
* Add Code of Conduct to generated projects
* Update CLJX: 0.4.0 =>  0.5.0
* Update Clojurescript: 0.0-2496 => 0.0-2644
* Fixes for regressions: [#71](https://github.com/plexus/chestnut/pull/71)

## v0.7.0-SNAPSHOT-20141226

* Update Clojurescript: 0.0-2371 =>  0.0-2496
* Update Compojure: 1.2.0 =>  1.3.1
* Update Om: 0.7.0 =>  0.8.0-beta3
* Fixes for regressions [#65](https://github.com/plexus/chestnut/pull/65) [#57](https://github.com/plexus/chestnut/pull/57)

## v0.7.0-SNAPSHOT-20141207

* Make weasel print both to the REPL and the browser console
  ([Marcus Lewis](https://github.com/mrcslws))
* Add support for the LESS CSS pre-processor
  ([Denis Golovnev](https://github.com/teur))
* Enable auto-reload of enlive templates in dev mode
  ([Ray H](https://github.com/rymndhng))
* Add support for the SASS CSS pre-processor
  ([Edward Wible](https://github.com/aew)
* Add suport for Speclj ([Edward Wible](https://github.com/aew)
* Switch from the deprecated compojure.handler to ring-defaults
  [zakak](https://github.com/zakak)
* Keep dev dependencies (Leiningen, Figwheel, Weasel, Speclj) out of
  the Uberjar
* Automatically switch the browser-repl to the right namespace after
  starting up, instead of `cljs.user`
* No longer include lein-ancient, easy enough to add for those that
  want it
* Update Ring: 1.3.1 => 1.3.2
* Update Figwheel: 0.1.4-SNAPSHOT => 0.1.6-SNAPSHOT
* Update Weasel: 0.4.0-SNAPSHOT => 0.4.2

## v0.6.0

* Add optional support for CLJX ([Olli Piepponen](https://github.com/luxbock))
* Support generation of projects named using the groupId/artifactId convention (e.g. com.example/foo) ([Steeve Beliveau](https://github.com/stebel))

## v0.5.0

* Run figwheel inside `(run)` so we only need one process
* Configure figwheel's CSS reloading and load a placeholder `style.css`
* Refresh Om when Figwheel reloads
* Update ClojureScript: 0.0-2342 => 0.0-2371
* Update Compojure: 1.1.9 => 1.2.0
* Update Om: 0.7.1 => 0.7.3
* No longer depend on Weasel in production mode

## v0.4.0

* Option to switch to HTTP Kit for a web server
* Add reloading middleware
* Add default compojure.handler.site middleware

## v0.3.0

* Switched to Weasel for Austin
* Optimized uberjar
* Fix usage of {{name}}/{{sanitized}}
* Load react from the jar, instead of from Facebook's CDN
* Update dependencies (Clojurescript, Ring, Compojure, Environ)

## v0.2.0

* Uberjar support
* Heroku support (Procfile, system.properties)
* added .gitignore
* First version of development/production modes

## v0.1.0

* First release, containing Austin, Figwheel, Om
