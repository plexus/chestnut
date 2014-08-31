# {{name}}


## Development

Start a REPL (in a terminal: `lein repl`, or from Emacs: open a clj/cljs file in the project, then do `M-x cider-jack-in`).

In the REPL do

```clojure
(run)
(browser-repl)
```

The first starts the webserver at 10555. The second starts the austin REPL server at a random port.

In a terminal do `lein figwheel`, this will watch and recompile your ClojureScript, and start the figwheel server on port 3449 (the default). Whenever your code changes, figwheel will recompile it and send it to the browser immediately.

Now browse to `http://localhost:10555` and enjoy.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
