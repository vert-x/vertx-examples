(defproject eventbusbridge-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1885"]
                 [io.vertx/clojure-api "0.3.0"]
                 [enfocus "2.0.0-beta1"]]
  :repositories [["sonatype snapshots" ;; for clojure-api snapshots
                  "https://oss.sonatype.org/content/repositories/snapshots"]]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "target/client.js"}}]})
