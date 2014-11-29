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

[Go to the documentation](http://plexus.github.io/chestnut/)

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

## Documentation

[Go to the documentation](http://plexus.github.io/chestnut/)

## License

Copyright © 2014 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
