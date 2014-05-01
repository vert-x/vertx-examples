(defproject eventbusbridge-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-1885"]
                 [io.vertx/clojure-api "1.0.2"]
                 [enfocus "2.0.0-beta1"]]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "target/client.js"}}]})
