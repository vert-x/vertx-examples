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

(ns example.sockjs.sockjs
  (:require [vertx.http :as http]
            [vertx.stream :as stream]
            [vertx.net :as net]
            [vertx.http.sockjs :as sockjs]))

(defn req-handler [req]
  (when (= "/" (.path req))
    (http/send-file (http/server-response req) "sockjs/index.html")))

(let [server (-> (http/server)
                 (http/on-request req-handler))]
  (-> (sockjs/sockjs-server server)
      (sockjs/install-app {:prefix "/testapp"}
                          #(stream/on-data % (partial stream/write %))))
  (http/listen server 8080 "localhost"
    (fn [ex _]
        (if ex
          (println ex)
          (println "Started HTTP server on localhost:8080")))))
