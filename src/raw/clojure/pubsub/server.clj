;; Copyright 2013 the original author or authors.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;      http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns example.pubsub.server
  (:require [vertx.net :as net]
            [vertx.shareddata :as shared]
            [clojure.string :as string]
            [vertx.stream :as stream]
            [vertx.eventbus :as bus]
            [vertx.buffer :as buf]))


(defn connect-handler [sock]
  (stream/on-data
   sock
   (fn [buf!]
     (buf/parse-delimited
      buf! "\n"
      (fn [line]
        (let [[cmd topic msg] (-> line str string/trim (string/split #","))
              topic-set! (and topic (shared/get-set topic))
              sock-id (.writeHandlerID sock)]
          (condp = cmd
            "subscribe" (do
                          (println "subscribing to" topic)
                          (shared/add! topic-set! sock-id))

            "unsubscribe" (do
                            (println "unsubscribing from" topic)
                            (shared/remove! topic-set! sock-id))

            "publish" (do
                        (println "publishing to" topic "with" msg)
                        (doseq [target topic-set!]
                          (bus/send target (buf/buffer (str msg "\n")))))

            (stream/write sock (format "unknown command: %s\n" cmd)))))))))

(-> (net/server)
    (net/on-connect connect-handler)
    (net/listen 1234 "localhost"
      (fn [ex _]
        (if ex
          (println ex)
          (println
            "Started pubsub server on localhost:1234.

Telnet in, and try the following commands:

To subscribe to a topic:

subscribe,<topic_name>

To unsubscribe from a topic:

unsubscribe,<topic_name>

To publish a message to a topic:

publish,<topic_name>,<message>

Where:

<topic_name> is the name of a topic

<message is some string you want to publish")))))
