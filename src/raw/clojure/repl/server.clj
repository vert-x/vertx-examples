(ns examples.repl.server
  (:require [vertx.repl :as repl]
            [vertx.core :as core]
            [vertx.http :as http]))

(def repl-server-id (atom nil))

(defn- req-handler [req]
  (println "Got request:" (.uri req))
  (println "Headers are:" (pr-str (http/headers req)))
  (-> (http/server-response req)
      (http/add-header "Content-Type" "text/html; charset=UTF-8")
      (http/end "<html><body><h1>Hello from vert.x!</h1></body></html>")))

(defn start-http-server []
  (-> (http/server)
      (http/on-request req-handler)
      (http/listen 8080 "localhost")))

(defn stop-repl-server [] (repl/stop @repl-server-id))

(reset! repl-server-id (repl/start))

;;after startup repl server, you could get connection with nrepl based on terminal
;;then could invoke start-http-server to startup a httpserver
;;or shutdown the server with fn stop-repl-server
