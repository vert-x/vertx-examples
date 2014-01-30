(ns embedded.core
  (:require [vertx.embed :as vertx]
            [vertx.eventbus :as eb])
  (:import java.util.concurrent.CountDownLatch))

(defn -main
  [& args]
  (vertx/set-vertx! (vertx/vertx))
  (let [latch (CountDownLatch. 1)]
    (eb/on-message "embed.example"
                   (fn [m]
                     (println "RCVD:" m)
                     (.countDown latch)))
    (eb/send "embed.example" "This works!")
    (.await latch)))
