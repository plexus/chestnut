# chestnut

A Leiningen template for a ClojureScript setup with Figwheel, Austin, Om.

## Sources

I used the [browser-connected-repl](https://github.com/cemerick/austin/tree/master/browser-connected-repl-sample) that's included with [Austin](https://github.com/cemerick/austin) as a starting point, then pulled in bits from [cljs-liveedit-webapp](https://github.com/ejlo/cljs-liveedit-webapp) until things worked.

If you're using this from Emacs make sure your CIDER is up to date.

## Usage

```
lein new chestnut <name>
```

After that open the README of your new project for further instructions.

tl;dr

```
$ lein repl

(run)
(browser-repl)
```

```
$ lein figwheel
```

Browse to `http://localhost:10555`

## Local copy

To run a local copy (if you want to customize Chestnut):

``` sh
git clone https://github.com/plexus/chestnut.git
cd chestnut
lein install
```

## License

Copyright © 2014 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
