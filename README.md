# chestnut

[![Clojars Project](http://clojars.org/chestnut/lein-template/latest-version.svg)](http://clojars.org/chestnut/lein-template)

A Leiningen template for a ClojureScript setup with Figwheel, Austin, Om.

## Usage

```
lein new chestnut <name>
```

After that open the README of your new project for further instructions.

## tl;dr

```
$ lein repl

(run)
(browser-repl)
```

```
$ lein figwheel
```

Browse to [http://localhost:10555](http://localhost:10555).

## List of Contents

This template gives you everything you need to start developing
Clojure/ClojureScript apps effectively. It comes with

* [Figwheel](https://github.com/bhauman/lein-figwheel) Automatically
  reload your ClojureScript code as soon as you save the file, no need
  for browser refresh.
* [Austin](https://github.com/cemerick/austin) Next generation browser
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

## Local copy

To run a local copy (if you want to customize Chestnut):

``` sh
git clone https://github.com/plexus/chestnut.git
cd chestnut
lein install
```

## Sources

I used the
[browser-connected-repl](https://github.com/cemerick/austin/tree/master/browser-connected-repl-sample)
that's included with [Austin](https://github.com/cemerick/austin) as a
starting point, then pulled in bits from
[cljs-liveedit-webapp](https://github.com/ejlo/cljs-liveedit-webapp)
until things worked.

For Heroku support I looked at Heroku's
[clojure-getting-started](https://github.com/heroku/clojure-getting-started)
example app.

## License

Copyright © 2014 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
