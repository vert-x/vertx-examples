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

(ns example.https.server
  (:require [vertx.http :as http]))

(defn req-handler [req]
  (println "Got request:" (.uri req))
  (println "Headers are:" (pr-str (http/headers req)))
  (-> (http/server-response req)
      (http/add-header "Content-Type" "text/html; charset=UTF-8")
      (http/end "<html><body><h1>Hello from vert.x! in SSL</h1></body></html>")))

(-> (http/server {:SSL true :key-store-path "server-keystore.jks" :key-store-password "wibble"})
    (http/on-request req-handler)
    (http/listen 4443 "localhost"))

(println "Starting Https server on localhost:4443")

