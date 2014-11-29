# List of Contents

Chestnut mostly combines the work of others to give you everything you
need. It's good to have a basic idea of what each of these does.

## React

[React](http://facebook.github.io/react/) is a library for building
user interfaces. It was created by Facebook to help them develop their
applications.

Its distinguising features are reactive data flow, and a virtual
DOM. This means your UI automatically updates when your data
changes. The virtual DOM lets React very efficiently figure out the
changes in the UI. In React you define components that each render a
piece of data.

## Om

[Om](https://github.com/swannodette/om) provides a ClojureScript
interface to React. By relying on Clojurescripts's persistent data
structures it can be even faster than plain react.

There are a few other Clojurescript wrappers for React, each with
their merits. But if you're using Chestnut, you get Om.


## Figwheel

[Figwheel](https://github.com/bhauman/lein-figwheel) powers the
instant reloading of Clojurescript and CSS. When the application loads
in the browser, it will connect to the Figwheel server. Whenever you
save a Clojurescript or CSS file, Figwheel will recompile it, and send
it to the browser. No page refresh required!

## Weasel

In Clojure and other Lisps it's common to make heavy use of a REPL
(read-eval-print-loop, i.e. a command line interface) during
development. Evaluating Clojurescript requires a Javascript
environment, such as provided by your browser.

Weasel gives you a REPL for Clojurescript. It will wait for a browser
to load and connect, so that it can send code to the browser to be
evaluated. This way you can interact live with your running application.

## Ring

Many web applications also have a server-side component, even for just
an API. The HTML page containing the Clojurescript app also needs to
be served from somewhere. There are a number of HTTP servers available
for this. Chestnut gives you the choice between Jetty and HTTP Kit.

[Ring](https://github.com/ring-clojure/ring) provides a common
interface for your app to communicate with a web server.

Ring also makes it easy to set up middleware that does extra
processing as part of a web request/response cycle. Chestnut sets up a
Ring middleware that handles hot reloading of server-side code.

## Compojure

[Compojure](http://weavejester.github.io/compojure/) is a "concise
routing library for Ring". It lets you configure request handlers
based on HTTP method and URL patterns.

## Enlive

[Enlive](https://github.com/cgrand/enlive) a selector based HTML
templating and transformation system. We use this to inject some
development workflow related bits into `index.html`.
