# chestnut

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/plexus/chestnut?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Clojureverse](https://rawgit.com/clojureverse/clojureverse-assets/master/clojureverse-org-green.svg)](http://clojureverse.org/c/chestnut)

[![Clojars Project](http://clojars.org/chestnut/lein-template/latest-version.svg)](http://clojars.org/chestnut/lein-template)

![Mr. Chestnut](resources/chestnut.png)

Chestnut is a Leiningen template for a Clojure/ClojureScript app based
on Om or Reagent, featuring a great dev setup, and easy deployment.

For smooth development you get instant reloading of Clojure,
ClojureScript, and CSS. A browser-connected REPL is also included.

For deployment you get uberjar support, meaning you can get all your
code compiled, optimized, and packaged in a single executable JAR
file. It also contains the necessary artifacts to work on Heroku out
of the box.

**Need help?** Ask on the mailing list (please do not open an issue on
GitHub): http://chestnut.aren.io/

[Go to the documentation](http://plexus.github.io/chestnut/)

This README may describe unreleased features. Please compare the
version number on Clojars to the changelog below, and check the README
in your generated project for instructions pertaining to your version.

## Documentation

[Go to the documentation](http://plexus.github.io/chestnut/)

## Usage

```
lein new chestnut <name>
```

After that open the README of your generated project for detailed
instructions, or consult the [Documentation](http://plexus.github.io/chestnut/)

### Lighttable

Lighttable provides a tighter integration for live coding with an inline
browser-tab. Rather than evaluating cljs on the command line with weasel repl,
evaluate code and preview pages inside Lighttable.

Steps: After running `(run)`, open a browser tab in Lighttable. Open a cljs file
from within a project, go to the end of an s-expression and hit Cmd-ENT.
Lighttable will ask you which client to connect. Click 'Connect a client' and
select 'Browser'. Browse to [http://localhost:3449](http://localhost:3449)

View LT's console to see a Chrome js console.

Hereafter, you can save a file and see changes or evaluate cljs code (without
saving a file). Note that running a weasel server is not required to evaluate
code in Lighttable.

### Emacs/Cider

Start a repl in the context of your project with `M-x cider-jack-in`.

Switch to repl-buffer with `C-c C-z` and start web and figwheel servers with
`(run)`, and weasel server with `(browser-repl`). Load
[http://localhost:3449](http://localhost:3449) on an external browser, which
connects to weasel, and start evaluating cljs inside Cider.

## List of Contents

This template gives you everything you need to start developing
Clojure/ClojureScript apps effectively. It comes with

* [Figwheel](https://github.com/bhauman/lein-figwheel) Automatically
  reload your ClojureScript and CSS as soon as you save the file, no
  need for browser refresh.
* [Om](https://github.com/swannodette/om) ClojureScript interface to Facebook's
  React. Alternatively you can use Reagent (`--reagent`), or use `--vanilla` to
  do without a React wrapper.
* [Ring](https://github.com/ring-clojure/ring) Clojure's de facto HTTP
  interface. Chestnut uses a Jetty or HttpKit server to serve the
  Clojurescript app. This way you already have an HTTP server running
  in case you want to add server-side functionality. Chestnut also
  inserts a Ring middleware to reload server-side Clojure files.
* Heroku support. Chestnut apps have all the bits and pieces to be
  deployable to Heroku. Getting your app on the web is as simple as
  `git push`.
* Unit tests for both Clojure and CLJS.
  Both specs and CLJS tests can be run in "auto" mode.

## Options

* `--reagent` Use Reagent instead of Om
* `--vanilla` Don't include Om, use this if you intend to use some other view library
* `--http-kit` Use [HTTP Kit](http://http-kit.org/server.html) instead
  of Jetty
* `--site-middleware` Use the `ring.middleware.defaults.site-defaults` middleware
  (session, CSRF), instead of `ring.middleware.defaults.api-defaults` (see
  [ring.defaults documentation](https://github.com/ring-clojure/ring-defaults))
* `--less` Use [less](https://github.com/montoux/lein-less) for
  compiling Less CSS files.

Use `--` to separate these options from Leiningen's options,
e.g. `lein new chestnut foo --snapshot -- --http-kit`

## Local copy

If you want to customize Chestnut, or try unreleased features, you can
run directly from master like this:

``` sh
git clone https://github.com/plexus/chestnut.git
cd chestnut
lein install
```

Note that master may be partially or wholly broken. I try to do
extensive manual testing before releasing a new stable version, so if
you don't like surprises then stick to the version on Clojars. Issue
reports and pull requests are very welcome.

## Requirements

* Java 1.7 or later
* Leiningen 2

## FAQ

* **Q:** How can I get the features in the SNAPSHOT version? <br>
  **A:** Use leiningen's `--snapshot` flag, e.g. `lein new chestnut
         my-project --snapshot`
* **Q:** I'm seeing warnings while compiling ClojureScript. <br>
  **A:** There are a few known warnings, but they should not affect the
         functioning of your app.
* **Q:** I changed the `{:text "Hello Chestnut!"}` portion and saved
         the file, but the changes don't show up. <br>
  **A:** It's a feature. The `app-state` is defined with `defonce`, so your
         application state doesn't reset every time you save a file. If you do
         want to reset after every change, change `(defonce app-state ..)` to
         `(def app-state ...)`.
* **Q:** I just want to compile ClojureScript to fully optimized
         JavaScript, so I can use it in a static HTML site. <br>
  **A:** Invoke cljsbuild with the uberjar profile active, like this: `lein
         with-profile -dev,+uberjar cljsbuild once`, then look for
         `resources/public/js/app.js`.
* **Q:** I'm getting `CompilerException java.lang.IllegalAccessError:
         in-seconds does not exist` when using Spyscope 0.1.4 or earlier.<br>
  **A:** Upgrade to [Spyscope 0.1.5](https://github.com/dgrnbrg/spyscope/issues/15), this
         issue is caused by an outdated dependency on cljs-time.
* **Q:** I upgraded the version of Om in project.clj, but it seems I'm
         still using the old version, what's up?<br>
  **A:** If you already did a
         build before, cljsbuild/figwheel won't pick up on the updated version
         automatically. Do a `lein cljsbuild clean`, then start Figwheel again.
* **Q:** The Figwheel output is cluttering up my REPL, can I get rid of it?<br>
  **A:** Not as such, but instead of calling `(run)`, you can call `(run-web-server)`
         and `(run-auto-reload)` in separate repls.
* **Q:** I gave my project a very generic name like `cljs` or `clojure` and now
         it's not working. <br>
  **A:** This is due to namespace clashes. Try picking
         a more unique name. In particular avoid namespace prefixes used by
         Clojure, Clojurescript, or existing libraries.

## Changelog

### 0.13.0-SNAPSHOT

* Avoid using Leiningen profile merging for `:cljsbuild` configs

### 0.12.0

* Upgrades
  * org.omcljs/om 1.0.0-alpha31 => 1.0.0-alpha35
  * lein-auto 0.1.1 => 0.1.2
  * lein-less 1.7.3 => 1.7.5
  * org.clojure/clojurescript 1.8.40 => 1.8.51
  * environ 1.0.2 => 1.0.3
  * lein-cljsbuild 1.1.1 => 1.1.3
  * lein-environ 1.0.1 => 1.0.3
  * figwheel 0.5.2 => 0.5.3-2
* Only add "dev" to the classpath in dev mode, fixes an issue with `lein jar`

### 0.11.0

* Upgrade ClojureScript 1.7.228 => 1.8.40
* Upgrade Figwheel 0.5.1 => 0.5.2

### 0.10.1

* Fix missing dependency when using `--http-kit`

### 0.10.0

* Add switches for --reagent and --vanilla
* Upgrade Figwheel to 0.5.1
* Upgrade to Clojure 1.8.0, Om 1.1.0-alpha31, ring-defaults 0.2.0, compojure 1.5.0

### v0.9.1

* Upgrade Figwheel to 0.5.0-6
* Upgrade Environ to 1.0.2
* Upgrade internal dependencies to play better with Boot

### v0.9.0

* Use Figwheel for REPL and Ring server
* Remove boilerplate that's no longer needed
* Drop Enlive and simplify the dev setup
* Drop CLJX support
* Better inline documentation
* Upgrade all dependencies

### v0.8.2-SNAPSHOT
* Version upgrades: clojurescript: 1.7.145 => 1.7.189, om: 1.0.0-alpha15 => 1.0.0-alpha28, figwheel => 0.4.1, figwheel-sidecar 0.4.1 => 0.5.0-2, lein-figwheel 0.4.1 =>  0.5.0-2
* Version upgrades: clojurescript: 1.7.122 => 1.7.145, om: 0.9.0 => 1.0.0-alpha15, tools.nrepl => 0.2.12, figwheel => 0.4.1, figwheel-sidecar => 0.4.1, lein-figwheel => 0.4.1,environ => 1.0.1, lein-environ => 1.0.1
* Fix allowing projects to have - in their names.
* Fix allowing groupID/artifactID naming convention.
* Fix "No such namespace" when using om via the REPL.
* Fix specljs auto testing.
* Added gzip compression using [ring.middleware.gzip](https://github.com/bertrandk/ring-gzip).
* Added browser cache-control hinting using [ring.middleware.browser-caching](https://github.com/slester/browser-caching).
* Version upgrades: clojure: 1.6.0 => 1.7.0, clojurescript: 0.0-3058 => 1.7.48, ring: 1.3.2 => 1.4.0, ring-defaults: 0.1.4 => 0.1.5, compojure: 1.3.2 => 1.4.0, om: 0.8.8 => 0.9.0, figwheel: 0.2.5 => 0.3.7, piggieback: 0.1.5 => 0.2.1, weasel: 0.6.0 => 0.7.0

### v0.8.1

* Restore working lein repl for cljx projects ([Josh Daghlian](https://github.com/jcdcodes))
* Use latest version of enlive to fix windows issue ([Justin Squirek](https://github.com/AdaDoom3))

### v0.8.0

* Update Om: 0.8.0-rc1 => 0.8.8 ([Anna Pawlicka](https://github.com/annapawlicka))
* Make Uberjar executable ([Børge Svingen](https://github.com/bsvingen))
* Remove react.js injection in dev html ([Jamie English](https://github.com/english))
* Fix how Less and Sassc are invoked ([Daryl Lau](https://github.com/dlau))
* Fix in figwheel config ([Tim Gilbert](https://github.com/timgilbert)), ([Philip Joseph](https://github.com/lambdahands)), ([Antony Woods](https://github.com/acron0))
* Fix in test setup ([Geoff Shannon](https://github.com/RadicalZephyr))
* Doc fixes ([Anthony Rosequist](https://github.com/arosequist))
* Version upgrades: clojurescript: 0.0-2511 => 0.0-3058 ; figwheel: 0.2.1-SNAPSHOT => 0.2.5 ; weasel: 0.4.2 => 0.6.0 and more

### v0.7.0

* Added Clojure{,script} unit testing support with Phantom JS.
  ([Rory Gibson](https://github.com/rorygibson))
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

### v0.7.0-SNAPSHOT-20141226

* Update Clojurescript: 0.0-2371 =>  0.0-2496
* Update Compojure: 1.2.0 =>  1.3.1
* Update Om: 0.7.0 =>  0.8.0-beta3
* Fixes for regressions: [#65](https://github.com/plexus/chestnut/pull/65) [#57](https://github.com/plexus/chestnut/pull/57)

### v0.7.0-SNAPSHOT-20141207

* Make weasel print both to the REPL and the browser console
  ([Marcus Lewis](https://github.com/mrcslws))
* Add support for the LESS CSS pre-processor
  ([Denis Golovnev](https://github.com/teur))
* Enable auto-reload of enlive templates in dev mode
  ([Ray H](https://github.com/rymndhng))
* Add support for the SASS CSS pre-processor
  ([Edward Wible](https://github.com/aew))
* Add suport for Speclj ([Edward Wible](https://github.com/aew))
* Switch from the deprecated compojure.handler to ring-defaults
  [zakak](https://github.com/zakak))
* Keep dev dependencies (Leiningen, Figwheel, Weasel, Speclj) out of
  the Uberjar
* Automatically switch the browser-repl to the right namespace after
  starting up, instead of `cljs.user`
* No longer include lein-ancient, easy enough to add for those that
  want it
* Update Ring: 1.3.1 => 1.3.2
* Update Figwheel: 0.1.4-SNAPSHOT => 0.1.6-SNAPSHOT
* Update Weasel: 0.4.0-SNAPSHOT => 0.4.2

### v0.6.0

* Add optional support for CLJX
  ([Olli Piepponen](https://github.com/luxbock))
* Support generation of projects named using the groupId/artifactId
  convention (e.g. com.example/foo)
  ([Steeve Beliveau](https://github.com/stebel))

### v0.5.0

* Run figwheel inside `(run)` so we only need one process
* Configure figwheel's CSS reloading and load a placeholder
  `style.css`
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
until things worked. Figwheel's
[Flappy Bird Demo app](https://github.com/bhauman/flappy-bird-demo)
also provided some ideas. The concept of refreshing Om when Figwheel
reloads was taken from
[this blog post](http://blog.michielborkent.nl/blog/2014/09/25/figwheel-keep-Om-turning/)
by [Michiel Borkent](https://github.com/borkdude).

For Heroku support I looked at Heroku's
[clojure-getting-started](https://github.com/heroku/clojure-getting-started)
example app.

## License

Copyright © 2014-2016 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
