# Testing

(This page is about testing Chestnut itself, *not* about how to do testing in your Chestnut app.)

Because of the nature of Chestnut, testing it requires an interplay between terminal, repl, and browser. While not impossible to automate, it is hard to do well, so for the time being we rely on manual testing to verify all features are working as expected before a release.

Doing this kind of testing is the most time consuming part of maintaining Chestnut, and it's one of the best things to do to help the project.

## Basic Test

This needs to be done for each of the feature flags.

* generate an app
* lein repl
* (run)
* (browser-repl)
* load the page, you should see "Hello, Chestnut"
* check the browser connected repl (e.g. `@app-state`)
* change some clojurescript (e.g. `s/h1/h2/`), watch the browser update
* change some CSS, watch it reload
* change something in index.html, watch it reload

## Production / Heroku test

This needs to be done for each of the feature flags.

* generate an app
* build an uberjar
* run with foreman, verify it works
* push to heroku, verify it works
