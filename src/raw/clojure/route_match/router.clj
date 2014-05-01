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

(ns example.route-match.router
  (:require [vertx.http :as http]
            [vertx.http.route :as rm]))

(-> (http/server)
    (http/on-request
     (-> (rm/get "/details/:user/:id"
                 #(let [params (http/params %)]
                    (http/end (http/server-response %)
                              (str "User: " (:user params)
                                   " ID: " (:id params)))))

         ;;Catch all - serve the index page
         (rm/all #".*"
                 #(http/send-file (http/server-response %)
                                  "route_match/index.html"))))
    (http/listen 8080 "localhost"
      (fn [ex _]
        (if ex
          (println ex)
          (println "Started HTTP server on localhost:8080")))))
