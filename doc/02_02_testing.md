# Testing Your App

By default, a new project created with Chestnut will have regular Clojure and ClojureScript unit tests enabled, the latter using lein-doo to run against various JS backends.

## Default ("deftest" style)
### Clojure unit tests
The Clojure unit tests can be found under ```test/clj```.
They're written using the [Clojure test API]([clojure.test](https://clojure.github.io/clojure/clojure.test-api.html)).
There's an example (*example_test.clj*) ready to be modified as soon as you create the project.

Run  the tests by executing

```bash
lein test
```

### ClojureScript tests
Can be found under ```test/cljs```.
Again, there's an example, in this case called *core.cljs*. Find out about the syntax by reading [the test framework source](https://github.com/clojure/clojurescript/blob/master/src/cljs/cljs/test.cljs)

These tests are set up to run using [PhantomJS](http://phantomjs.org/), so make sure you have that installed and available on your path.

Then execute

```bash
lein doo phantom test once
```

If you want to have auto-testing (where the tests automatically run when you make a change to an affected namespace) then use

```bash
lein doo phantom test
```
