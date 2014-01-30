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

(ns example.echo.echo-client
  (:require [vertx.core :as vertx]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.buffer :as buf]))

(defn send-data [socket]
  (stream/on-data socket #(println "echo client received:" %))
  (doseq [i (range 10)]
    (let [s (format "hello %s\n" i)]
      (println "echo client sending:" s)
      (stream/write socket s))))

(println "Connecting to localhost:1234")
(net/connect
 1234 "localhost"
 (fn [err socket]
   (if err
     (throw (:basis err))
     (send-data socket))))
