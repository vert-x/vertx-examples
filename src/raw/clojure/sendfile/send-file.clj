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

(ns example.sendfile.send-file
  (:require [vertx.http :as http]))

(-> (http/server)
    (http/on-request
     (fn [req]
       (let [root-path "sendfile/"
             resp (http/server-response req)]
         (if (= "/" (.path req))
           (http/send-file resp (str root-path "index.html"))
           (http/send-file resp (str root-path (.path req)))))))
    (http/listen 8080 "localhost"
      (fn [ex _]
        (if ex
          (println ex)
          (println "Started HTTP server on localhost:8080")))))
