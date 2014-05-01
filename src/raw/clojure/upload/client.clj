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

(ns example.upload.client
  (:require [vertx.http :as http]
            [vertx.stream :as stream]
            [vertx.utils :as u]
            [vertx.filesystem.sync :as fss]
            [vertx.filesystem :as fs]))

(let [filename "upload/upload.txt"
      client (http/client {:port 8080 :host "localhost"})
      req (http/put client "/some-url"
                    #(println "File uploaded" (.statusCode %)))]

  ;; For a chunked upload you don't need to specify size
  ;; just set req to chunked true
  (http/add-header req "Content-Length" (str (:size (fss/properties filename))))

  (fs/open filename
           (fn [err file]
             (stream/pump file req)
             (stream/on-end file
                            (fn []
                              (fs/close file
                                        (fn [err]
                                          (if err
                                            (throw err)
                                            (println "Sent request"))))
                              (http/end req))))))
