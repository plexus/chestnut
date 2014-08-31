# chestnut

A Leiningen template for a ClojureScript setup with Figwheel, Austin, Om.

## Sources

I used the [browser-connected-repl](https://github.com/cemerick/austin/tree/master/browser-connected-repl-sample) that's included with [Austin](https://github.com/cemerick/austin) as a starting point, then pulled in bits from [cljs-liveedit-webapp](https://github.com/ejlo/cljs-liveedit-webapp) until things worked.

If you're using this from Emacs make sure your CIDER is up to date.

## Usage

At the moment Chestnut is not yet registered as a leiningen plugin, so you have to install it locally first.

```
git checkout https://github.com/plexus/chestnut
cd chestnut
lein install
```

After that you can create your Om project!

```
lein new chestnut <name>
```

Further instructions on how to get started can be found in the README of your new project.

## License

Copyright © 2014 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
