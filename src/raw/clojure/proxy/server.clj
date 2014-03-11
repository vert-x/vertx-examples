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

(ns example.proxy.server
  (:require [vertx.http :as http]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]))

(defn req-handler [req]
  (println "Got request:" (.uri req))
  (println "Headers are:" (pr-str (http/headers req)))
  (stream/on-data req #(println "Got data" %))
  (stream/on-end req #(let [resp (http/server-response req {:chunked true})]
                        (dotimes [n 10]
                          (stream/write resp (str "server-data-chunk-" n)))
                        (http/end resp))))

(-> (http/server)
    (http/on-request req-handler)
    (http/listen 8282 "localhost"))
