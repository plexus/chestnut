# chestnut

An example ClojureScript setup with Figwheel, Austin, Om.

## Usage

Start a REPL (`lein repl` in a terminal or `M-x cider-jack-in` from Emacs). In the REPL do

```
(run)
(browser-repl)
```

The first starts the webserver at 10555. The second starts the REPL server at a random port.

In a terminal do `lein figwheel`, this will watch and recompile your ClojureScript, and start the figwheel server.

Now browse to `http://localhost:10555` and enjoy.

## License

Copyright © 2014 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
