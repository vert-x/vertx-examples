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

(ns example.proxy.client
  (:require [vertx.http :as http]
            [vertx.stream :as stream]
            [vertx.buffer :as buf]))


(defn write-data [req]
  (dotimes [n 10]
    (stream/write req (str "client-data-chunk-" n))) req)

(-> (http/client {:port 8080 :host "localhost"})
    (http/put "/some-url"
              #(stream/on-data %
                               (partial
                                (println "Got response data"))))
    (.setChunked true)
    write-data
    http/end)
