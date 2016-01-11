# Invoking Chestnut

Chestnut is a Leiningen template, you use it through the `lein new <template> <project name>`
command. When you tell `lein new` to use the
Chestnut template, it will download the latest stable version
[from Clojars](https://clojars.org/chestnut/lein-template), and then
use that to generate the app.

There are a number of command line flags you can pass in, some are
handled by `lein new`, some are handled by Chestnut. You should
separate them with `--` to not confuse Leiningen

``` sh
lein new chestnut <project name> <leiningen flags> -- <chestnut flags>
```

You can invoke `lein help new` for more info on Leiningen's flags. The
flags that Chestnut supports are listed below.

## Chestnut Flags

### `--http-kit`

Use [HTTP Kit](http://http-kit.org/server.html) as HTTP server,
instead of Jetty. If you're not sure why you would need this, then
you're probably fine sticking to the default.

### `--site-middleware`

We load some default Ring middleware to handle common concerns in web
applications. It is assumed your app will mainly consist of an API and
a Clojurescript client-side app, so only a minimal amount of
middleware is loaded, notable handling of urlencoded parameters.

If you plan to actually have a server side web app generating HTML
pages, you will want to use `--site-middleware`, which will also set
up middleware for sessions, cookies, xss protection, frame options
headers, static resources, etc.

### `--less`

Add support for the [less](https://github.com/montoux/lein-less) CSS preprocessor.

## SNAPSHOT

New features in Chestnut are first released in a special version with
a name ending in `-SNAPSHOT`. This signals to leiningen that this is a
development release that's not intended for the general public.

If you are already quite comfortable with Clojure and Clojurescript
development, we recommend running the snapshot version and file any
issues you run into, so we can make sure the next release of Chestnut
will be solid and stable.

To use the snapshot version, pass the `--snapshot` flag to Leiningen.

``` sh
lein new chestnut <project name> --snapshot -- <chestnut options>
```
