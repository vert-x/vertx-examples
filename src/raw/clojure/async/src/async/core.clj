(ns async.core
  (:import java.util.concurrent.CountDownLatch)
  (:require [vertx.embed :as vertx]
            [vertx.eventbus :as eb]
            [clojure.core.async :as async :refer [<! >! chan go go-loop]]))

(def address "example.address")

;; serialize output so it doesn't get interleaved
(let [log-chan (chan)]
  (defn log [& msg]
    (go
      (>! log-chan msg)))

  (defn log-output-loop []
    (go-loop [msg (<! log-chan)]
      (apply println msg)
      (recur (<! log-chan)))))

(defn- reply-processor [chan]
  (go-loop [msg (<! chan)]
    (log "sender received:" msg)
    (recur (<! chan))))

(defn bus-sender []
  (let [reply-chan (chan)]
    (dotimes [n 100]
      (eb/send address {:ping n}
        (fn [reply]
          (go (>! reply-chan reply))
          (reply-processor reply-chan))))))


(defn- message-processor [chan]
  (go-loop [msg (<! chan)]
    (log "listener received:" msg)
    (eb/reply {:pong (:ping msg)})
    (recur (<! chan))))

(defn bus-listener []
  (let [chan (chan)]
    (eb/on-message
      address
      (fn [msg]
        (go (>! chan msg))
        (message-processor chan)))))

(defn -main
  [& args]
  (vertx/set-vertx! (vertx/vertx))
  (log-output-loop)
  (let [latch (CountDownLatch. 1)]
    (bus-listener)
    (bus-sender)
    (.await latch)))
