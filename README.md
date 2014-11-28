# chestnut

[![Clojars Project](http://clojars.org/chestnut/lein-template/latest-version.svg)](http://clojars.org/chestnut/lein-template)

A Leiningen template for a Clojure/ClojureScript app based on Om,
featuring a great dev setup, and easy deployment.

For smooth development you get instant reloading of Clojure,
ClojureScript, and CSS. A browser-connected REPL is also included.

For deployment you get uberjar support, meaning you can get all your
code compiled, optimized, and packaged in a single executable JAR
file. It also contains the necessary artifacts to work on Heroku out
of the box.

This README may describe unreleased features. Please compare the
version number on Clojars to the changelog below, and check the README
in your generated project for instructions pertaining to your version.

## Usage

```
lein new chestnut <name>
```

After that open the README of your generated project for detailed instructions.

## tl;dr

```
$ lein repl

(run)
(browser-repl)
```

Wait a bit, then browse to [http://localhost:10555](http://localhost:10555).

## List of Contents

This template gives you everything you need to start developing
Clojure/ClojureScript apps effectively. It comes with

* [Figwheel](https://github.com/bhauman/lein-figwheel) Automatically
  reload your ClojureScript and CSS as soon as you save the file, no need
  for browser refresh.
* [Weasel](https://github.com/tomjakubowski/weasel) Next generation browser
  connected REPL. Try things out and manipulate your running app.
* [Om](https://github.com/swannodette/om) ClojureScript interface to
  Facebook's React.
* [Ring](https://github.com/ring-clojure/ring) Clojure's de facto HTTP
  interface. Chestnut uses a Jetty or HttpKit server to serve the
  Clojurescript app. This way you already have an HTTP server running
  in case you want to add server-side functionality. Chestnut also
  inserts a Ring middleware to reload server-side Clojure files.
* Heroku support. Chestnut apps have all the bits and pieces to be
  deployable to Heroku. Getting your app on the web is as simple as
  `git push`.

## Options

* `--http-kit` Use [HTTP Kit](http://http-kit.org/server.html) instead of Jetty
* `--site-middleware` Use the `compojure.handler.site` middleware (session, CSRF), instead of `compojure.handler.api` (see [compojure.handler documentation](http://weavejester.github.io/compojure/compojure.handler.html))
* `--om-tools` Use Prismatic's [om-tools.dom](https://github.com/Prismatic/om-tools) instead of `om.dom`
* `--cljx` Using [cljx](https://github.com/lynaghk/cljx) allows you to write code that is shared between Clojure and ClojureScript.
* `--less` Use [less](https://github.com/montoux/lein-less) for compiling Less CSS files.
* `--sass` Use [sass](https://bitbucket.org/apribase/lein-sassc) for compiling Sass CSS files.
* `--spec` Use [specljx](http://speclj.com) test runner for clj and cljs.

Use `--` to separate these options from Leiningen's options, e.g. `lein new chestnut foo -- --om-tools --http-kit`

## Local copy

If you want to customize Chestnut, or try unreleased features, you can run directly from master like this:

``` sh
git clone https://github.com/plexus/chestnut.git
cd chestnut
lein install
```

Note that master may be partially or wholly broken. I try to do extensive manual testing before releasing a new stable version, so if you don't like surprises then stick to the version on Clojars. Issue reports and pull requests are very welcome.

## Requirements

* Java 1.7 or later
* Leiningen 2

## FAQ

* **Q:** How can I get the features in the SNAPSHOT version? <br>
  **A:** Use leiningen's `--snapshot` flag, e.g. `lein new chestnut my-project --snapshot`
* **Q:** I'm seeing warnings while compiling ClojureScript. <br>
  **A:** There are a few known warnings, but they should not affect the functioning of your app.
* **Q:** I changed the `{:text "Hello Chestnut!"}` portion and saved the file, but the changes don't show up. <br>
  **A:** It's a feature. The `app-state` is defined with `defonce`, so your application state doesn't reset every time you save a file. If you do want to reset after every change, change `(defonce app-state ..)` to `(def app-state ...)`.
* **Q:** I just want to compile ClojureScript to fully optimized JavaScript, so I can use it in a static HTML site. <br>
  **A:** Invoke cljsbuild with the uberjar profile active, like this: `lein with-profile -dev,+uberjar cljsbuild once`, then look for `resources/public/js/app.js`.
* **Q** I'm getting `CompilerException java.lang.IllegalAccessError: in-seconds does not exist` when using Spyscope 0.1.4 or earlier.<br>
  **A** Upgrade to [Spyscope 0.1.5](https://github.com/dgrnbrg/spyscope/issues/15), this issue is caused by an outdated dependency on cljs-time.
* **Q** I upgraded the version of Om in project.clj, but it seems I'm still using the old version, what's up?<br>
  **A** If you already did a build before, cljsbuild/figwheel won't pick up on the updated version automatically. Do a `lein cljsbuild clean`, then start Figwheel again.

## Changelog

### v0.7.0-SNAPSHOT

* Add support for the LESS CSS pre-processor ([Denis Golovnev](https://github.com/teur))
* Add support for the SASS CSS pre-processor
* Add support for specljx tests
* Make weasel print both to the REPL and the browser console ([Marcus Lewis](https://github.com/mrcslws))
* Enable auto-reload of enlive templates in dev mode ([Ray H](https://github.com/rymndhng))

### v0.6.0

* Add optional support for CLJX ([Olli Piepponen](https://github.com/luxbock))
* Support generation of projects named using the groupId/artifactId convention (e.g. com.example/foo) ([Steeve Beliveau](https://github.com/stebel))

### v0.5.0

* Run figwheel inside `(run)` so we only need one process
* Configure figwheel's CSS reloading and load a placeholder `style.css`
* Refresh Om when Figwheel reloads
* Update ClojureScript: 0.0-2342 => 0.0-2371
* Update Compojure: 1.1.9 => 1.2.0
* Update Om: 0.7.1 => 0.7.3
* No longer depend on Weasel in production mode

### v0.4.0

* Option to switch to HTTP Kit for a web server
* Add reloading middleware
* Add default compojure.handler.site middleware

### v0.3.0

* Switched to Weasel for Austin
* Optimized uberjar
* Fix usage of {{name}}/{{sanitized}}
* Load react from the jar, instead of from Facebook's CDN
* Update dependencies (Clojurescript, Ring, Compojure, Environ)

### v0.2.0

* Uberjar support
* Heroku support (Procfile, system.properties)
* added .gitignore
* First version of development/production modes

### v0.1.0

* First release, containing Austin, Figwheel, Om

## Sources

I used the
[browser-connected-repl](https://github.com/cemerick/austin/tree/master/browser-connected-repl-sample)
that's included with [Austin](https://github.com/cemerick/austin) as a
starting point, then pulled in bits from
[cljs-liveedit-webapp](https://github.com/ejlo/cljs-liveedit-webapp)
until things worked. Figwheel's [Flappy Bird Demo app](https://github.com/bhauman/flappy-bird-demo) also provided some ideas. The concept of refreshing Om when Figwheel reloads was taken from [this blog post](http://blog.michielborkent.nl/blog/2014/09/25/figwheel-keep-Om-turning/) by [Michiel Borkent](https://github.com/borkdude).

For Heroku support I looked at Heroku's
[clojure-getting-started](https://github.com/heroku/clojure-getting-started)
example app.

## License

Copyright © 2014 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
