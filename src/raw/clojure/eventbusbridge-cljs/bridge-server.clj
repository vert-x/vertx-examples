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

(ns example.eventbusbridge.bridge-server
  (:require [vertx.core :as core]
            [vertx.http :as http]
            [vertx.http.sockjs :as sockjs]))

(defn req-handler [req]
  (condp = (.uri req)
      "/" (http/send-file (http/server-response req) "resources/index.html")
      "/client.js" (http/send-file (http/server-response req) "target/client.js")))

(let [server (-> (http/server)
                 (http/on-request req-handler))]
  ;;Create a SockJS bridge which lets everything through (be careful!)
  (-> (sockjs/sockjs-server server)
      (sockjs/set-hooks
       :closed (fn [sock] (println "handleSocketClosed, sock = " sock))

       :send (fn [sock msg address] (println "handleSend, sock = " sock
                                             "address = " address
                                             "msg = " msg) true)

       :publish (fn [sock msg address] (println "handlePub, sock = " sock
                                                "address = " address
                                                "msg = " msg) true)
       :pre-register (fn [sock address]
                       (println "handlePreRegister, sock = "sock", address = " address) true)

       :post-register (fn [sock address]
                        (println "handlePostRegister,sock = "sock", address = " address))

       :unregister (fn [sock address]
                     (println "handleUnregister,sock = "sock", address = "address) true))

      (sockjs/bridge {:prefix "/eventbus"} [{}] [{}]))

  (http/listen server 8080 "localhost"
               (println "Starting Http server on localhost:8080")))
