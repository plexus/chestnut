# Alternatives to om.dom

There are a few libraries available that provide alternatives to om.dom.

## om-tools

[Om-tools](https://github.com/Prismatic/om-tools) is a library by
Prismatic to make writing Om components easier.

First, in `project.clj`, add `om-tools` as a dependency

``` clojure
  :dependencies [# ...
                 [prismatic/om-tools "0.3.9" :exclusions [potemkin]]]
```

In `core.cljs`, replace `[om.dom :as dom :include-macros true]` with

``` clojurescript
[om-tools.dom :as dom :include-macros true]
[om-tools.core :refer-macros [defcomponent]]
```

If you still have a freshly generated Chestnut app, the only further change you need to do is remove the `nil` in `(dom/h1 nil (:text app))`.

Compared to `om.dom`, `om-tools.dom` does three things different for a cleaner syntax.

* element attributes can be omitted (that's why the `nil` is no longer necessary), and can be Clojurescript maps. So the `#js` reader macro is not needed
* attribute names are more Clojure-esque "natural". `:className` instead of `:class`, `:onClick` instead of `:on-click`
* children can be collections, so you don't need to use `apply` to when setting a collection as children of an element

Have a look at the [om-tools.dom docs](https://github.com/Prismatic/om-tools#dom-tools) for more examples.

## Sablono

[Sablono](https://github.com/r0man/sablono) provides Hiccup-style templating for Om.

First, in `project.clj`, add `sablono` as a dependency

``` clojure
  :dependencies [# ...
                 [sablono "0.2.22"]]
```

In `core.cljs`, replace `[om.dom :as dom :include-macros true]` with

``` clojurescript
[sablono.core :as html :refer-macros [html]]
```

This `h1` definition

``` clojurescript
(dom/h1 nil (:text app))
```

becomes

``` clojurescript
(html [:h1 (:text app)])
```
