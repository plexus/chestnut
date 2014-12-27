# Developer notes

## Local copy

If you want to customize Chestnut, or try unreleased features, you can run directly from master like this:

``` sh
git clone https://github.com/plexus/chestnut.git
cd chestnut
lein install
```

Note that master may be partially or wholly broken. I try to do
extensive manual testing before releasing a new stable version, so if
you don't like surprises then stick to the version on Clojars. Issue
reports and pull requests are very welcome.

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

## License

Copyright © 2014 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
