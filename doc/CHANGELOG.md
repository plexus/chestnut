# Changelog

## v0.7.0-SNAPSHOT

* Add support for the LESS CSS pre-processor ([Denis Golovnev](https://github.com/teur))
* Make weasel print both to the REPL and the browser console ([Marcus Lewis](https://github.com/mrcslws))
* Enable auto-reload of enlive templates in dev mode ([Ray H](https://github.com/rymndhng))

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
