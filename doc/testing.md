# Testing Chestnut

(This page is about testing Chestnut itself, *not* about how to do testing in your Chestnut app.)

Because of the nature of Chestnut, testing it requires an interplay between terminal, repl, and browser. This is hard to automate, but we're trying anyway :) There are some basic tests available, but the test suite is not comprehensive.

For process control the tests depend on unreleased features of [jnr-process](https://github.com/jnr/jnr-process), which in turn relies on unreleased code in [jnr-posix](https://github.com/jnr/jnr-posix). So clone those two repos and for each do `mvn install`. I *think* that's the only dependencies you need to set up. Leiningen will figure out the rest.

To run the tests do `lein test`. This will start with executing `cd /tmp ; lein new chestnut --snapshot`, possibly with some options. So to run the tests against your local version, you have to first do a `lein install`. Also make sure that if the tests are using `--snapshot`
, that the version you're installing ends with `-SNAPSHOT`.

For WebDriver, the tests are currently set up to use Chrome, and to look for the chromedriver executable in `~/bin/chromedriver`. This might not be suitable for your system. Check the [clj-webdriver](https://github.com/semperos/clj-webdriver) docs and make sure it's able to open a browser. See `src/chestnut/test/integration.clj` to twiddle with this. You can try this in a REPL to see if a browser pops up:

``` clojure
(System/setProperty "webdriver.chrome.driver" (str (System/getenv "HOME") "/bin/chromedriver"))
(use 'clj-webdriver.taxi)
(set-driver! {:browser :chrome} "https://github.com/plexus/chestnut")
```

Running these test *takes time*. There's a lot of JVM booting and shutting down, and it seems as the consecutive tests run this process actually becomes even slower. Because of this you might run into the problem where the test tries to boot a REPL, but this times out. To work around that you can add this to `src/leiningen/new/chestnut/project.clj`, after which you do a new `lein install`

``` clojure
:repl-options {:timeout 200000} ;; Defaults to 30000 (30 seconds)
```

## Basic Test

This is implemented now as an automated test, it will be executed for each of the feature flags.

* generate an app
* lein repl
* (run)
* (browser-repl)
* load the page, you should see "Hello, Chestnut"
* check the browser connected repl (e.g. `@app-state`)
* change some clojurescript (e.g. `s/h1/h2/`), watch the browser update
* change some CSS, watch it reload
* change something in index.html, watch it reload

## Production / Heroku test

This needs to be done for each of the feature flags.

* generate an app
* build an uberjar
* run with foreman, verify it works
* push to heroku, verify it works

## Feature tests

These are features, some hidden behind feature flags, that should be verified manually

* Clojure and Clojurescript unit test support
* CLJX
* speclj / specljs
* LESS
