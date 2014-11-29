# FAQ

* **Q:** How can I get the features in the SNAPSHOT version? <br>
  **A:** Use leiningen's `--snapshot` flag, e.g. `lein new chestnut my-project --snapshot`
* **Q:** I'm seeing warnings while compiling ClojureScript. <br>
  **A:** There are a few known warnings, but they should not affect the functioning of your app.
* **Q:** I changed the `{:text "Hello Chestnut!"}` portion and saved the file, but the changes don't show up. <br>
  **A:** It's a feature. The `app-state` is defined with `defonce`, so your application state doesn't reset every time you save a file. If you do want to reset after every change, change `(defonce app-state ..)` to `(def app-state ...)`.
* **Q:** I just want to compile ClojureScript to fully optimized JavaScript, so I can use it in a static HTML site. <br>
  **A:** Invoke cljsbuild with the uberjar profile active, like this: `lein with-profile -dev,+uberjar cljsbuild once`, then look for `resources/public/js/app.js`.
* **Q** I'm getting `CompilerException java.lang.IllegalAccessError: in-seconds does not exist` when using Spyscope 0.1.4 or earlier.<br>
  **A** Upgrade to [Spyscope 0.1.5](https://github.com/dgrnbrg/spyscope/issues/15), this issue is caused by an outdated dependency on cljs-time.
* **Q** I upgraded the version of Om in project.clj, but it seems I'm still using the old version, what's up?<br>
  **A** If you already did a build before, cljsbuild/figwheel won't pick up on the updated version automatically. Do a `lein cljsbuild clean`, then start Figwheel again.
