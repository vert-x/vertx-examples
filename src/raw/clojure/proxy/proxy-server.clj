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

(ns example.proxy.proxy-server
  (:require [vertx.http :as http]
            [vertx.stream :as stream]
            [vertx.buffer :as buf]))

(defn client-respense-handler [server-req client-resp]
  (println "Proxying response:" (.statusCode client-resp))
  (let [server-resp
        (-> server-req
            (http/server-response
             {:status-code (.statusCode client-resp)
              :chunked true})
            (http/add-headers (http/headers client-resp)))]
    (-> client-resp
        (stream/on-data
         (fn [buf!]
           (println "Proxying response body:" buf!)
           (stream/write server-resp buf!)))
        (stream/on-end
         #(http/end server-resp)))))

(defn req-handler [client req]
  (println "Proxying request:" (.uri req))
  (let [client-req
        (-> (http/request client
                          (.method req)
                          (.uri req)
                          (partial client-respense-handler req))
            (http/add-headers (http/headers req))
            (.setChunked true))]
    (-> req
        (stream/on-data
         (fn [buf!]
           (println "Proxying request body:" buf!)
           (stream/write client-req buf!)))
        (stream/on-end
         (fn []
           (println "end of the request")
           (http/end client-req))))))

(-> (http/server)
    (http/on-request
     (partial req-handler (http/client {:port 8282 :host "localhost"})))
    (http/listen 8080 "localhost"))
