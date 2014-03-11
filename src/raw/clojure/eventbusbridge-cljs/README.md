# eventbusbridge-cljs

An example of using the ClojureScript eventbus.

## Usage

First, compile the ClojureScript client:
  
    lein cljsbuild once
    
Then run the server:

    vertx run bridge-server.clj
   
And connect to http://localhost:8080/

See the implementation for the
[client](src/eventbusbridge_cljs/client.cljs) and the
[`vertx.client.eventbus`](https://github.com/vert-x/mod-lang-clojure/blob/master/api/src/main/clojure/vertx/client/eventbus.cljs).
