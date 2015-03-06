# Testing Your App

By default, a new project created with Chestnut will have regular Clojure and ClojureScript unit tests enabled,
but you can choose to use Speclj for both CLJ and CLJS if you prefer the RSpec style of writing tests.

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

These tests are set up to run using [PhantomJS](http://phantomjs.org/), so make sure you have that
installed and available on your path.

Then execute

```bash
lein cljsbuild test
```

If you want to have auto-testing (where the tests automatically run when you make a change
to an affected namespace) then open up a new terminal and execute

```bash
lein cljsbuild auto test
```



## Speclj style
If you prefer to write tests using the RSpec style, then create your project with the Speclj feature flag as follows:

```bash
lein new chestnut my-project -- --speclj
```

Then you can run the Clojure specs (available in ```spec/clj```) by executing

```bash
lein spec
```

and the ClojureScript specs (in ```spec/cljs```) will run by default whenever you build the CLJS:
``lein cljsbuild once```

Note that, as with the ClojureScript test style, the CLJS specs require PhantomJS to be installed and on your path.
