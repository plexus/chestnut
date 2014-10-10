# chestnut

[![Clojars Project](http://clojars.org/chestnut/lein-template/latest-version.svg)](http://clojars.org/chestnut/lein-template)

A Leiningen template for a Clojure/ClojureScript app, featuring a
great dev setup (browser-connected REPL, live code reloading), easy
deployment (generate optimized uberjar, run with Foreman).

Contains a starting point for using Om.

This README may describe unreleased features. Please compare the version number on Clojars to the changelog below, and check the README in your generated project for instructions pertaining to your version.

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
  reload your ClojureScript code as soon as you save the file, no need
  for browser refresh.
* [Weasel](https://github.com/tomjakubowski/weasel) Next generation browser
  connected REPL. Try things out and manipulate your running app.
* [Om](https://github.com/swannodette/om) ClojureScript interface to
  Facebook's React.
* [Ring](https://github.com/ring-clojure/ring) Clojure's de facto HTTP
  interface. Chestnut uses a Jetty server to serve the Clojurescript
  app. This way you already have an HTTP server running in case you
  want to add server-side functionality.
* Heroku support. Chestnut apps have all the bits and pieces to be
  deployable to Heroku. Getting your app on the web is as simple as
  `git push`.

## Options

* `--http-kit` Use [HTTP Kit](http://http-kit.org/server.html) instead of Jetty
* `--site-middleware` Use the `compojure.handler.site` middleware, instead of `compojure.handler.api` (see [compojure.handler documentation](http://weavejester.github.io/compojure/compojure.handler.html))
* `--om-tools` Use Prismatic's [om-tools.dom](https://github.com/Prismatic/om-tools) instead of `om.dom`

Use `--` to separate these options from Leiningen's options, e.g. `lein new chestnut foo -- --om-tools --http-kit`

## Local copy

If you want to customize Chestnut, or try unreleased features, you can run directly from master like this:

``` sh
git clone https://github.com/plexus/chestnut.git
cd chestnut
lein install
```

Note that master may be partially or wholly broken. I try to do extensive manual testing before releasing a new stable version, so if you don't like surprises then stick to the version on Clojars. Issue reports and pull requests are very welcome.

## Changelog

### v0.5.0 (unreleased)

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
