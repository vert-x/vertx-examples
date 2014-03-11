# mod-lang-clojure Examples


1) The bin directory from the vertx distro must be on your PATH - this
   should have been done as part of the install procedure.

2) JDK/JRE 1.7.0+ must be installed and the JDK/JRE bin directory must
   be on your PATH

3) Until a mod-lang-clojure artifact is published, you'll need to build
   and install it locally to run the examples.

*all examples should be run from this directory unless otherwise stated*

    vertx run <example script name>

where <example script name> is, for example, echo/echo-server.clj

## Echo Server

A simple echo server which echos back any sent to it

To run the server:

    vertx run echo/echo-server.clj

Then either telnet localhost 1234 and notice how text entered via
telnet is echoed back

Instead of telnet you can also run a simple echo client in a different
console:

    vertx run echo/echo-client.clj

## Fanout Server

Fans out all data received on any one connection to all other
connections.

To run the server:

    vertx run fanout/server.clj

Then telnet localhost 1234 from different consoles. Note how data
entered in telnet is echoed to all connected connections.

## HTTP

A simple HTTP server which just returns some hard-coded HTML to the
client, and a simple HTTP client which sends a GET request and
displays the response it receives.

To run the server:

    vertx run http/server.clj

Then point your browser at http://localhost:8080

Alternatively, you can also run the HTTP client from a different
console:

    vertx run http/client.clj

## HTTPS

Like the HTTP example, but using HTTPS

To run the server:

    vertx run https/server.clj

Then point your browser at https://localhost:4443

Alternatively, you can also run the HTTPS client from a different
console:

    vertx run https/client.clj

You'll get a warning from your browser since the server certificate
the server is using is not known to it, that's normal.

## Proxy

A very simple HTTP proxy which simply proxies requests/response from a
client to a server and back again.  Run each part in its own console:

It includes

a) A simple http server which just takes the request and sends back a
response in 10 chunks

b) A simple http client which sends a http request with 10 chunks (via
the proxy server), and displays any response it receives

c) A proxy server which simply sits in the middle proxying requests
and responses between client and server

Run the http server:

    vertx run proxy/server.clj

Run the proxy server:

    vertx run proxy/proxy-server.clj

Run the http client:

    vertx run proxy/client.clj

## PubSub

A very simple publish-subscribe server.

Connections can subscribe to topics and unsubscribe from
topics. Topics can be any arbitrary string.

When subscribed, connections will receive any messages published to
any of the topics it is subscribed to.

The pub-sub server understands the following simple text
protocol. Each line is terminated by CR (hit enter on telnet)

To subscribe to a topic:

subscribe,<topic_name>

To unsubscribe from a topic:

unsubscribe,<topic_name>

To publish a message to a topic:

publish,<topic_name>,<message>

Where:

<topic_name> is the name of a topic

<message is some string you want to publish

To run the server:

    vertx run pubsub/server.clj

Then open some more consoles and telnet localhost 1234, and experiment
with the protocol.

## SendFile

Simple web server that uses sendfile to serve content directly from
disk to the socket bypassing user space. This is a very efficient way
of serving static files from disk.

The example contains three static pages: index.html, page1.html and
page2.html which are all served using sendfile.

To run the server:

    vertx run sendfile/send-file.clj

Then point your browser at http://localhost:8080 and click around.

## SSL

This is like the echo example, but this time using SSL.

To run the server:

    vertx run ssl/server.clj

To run the client in a different console:

    vertx run ssl/client.clj

## Upload

A simple upload server example. The client streams a file from disk to
an HTTP request and the server reads the HTTP request and streams the
data to a file on disk.

To run the server:

    vertx run upload/server.clj

To run the client in a different console:

    vertx run upload/client.clj

## Websockets

A simple example demonstrating HTML 5 websockets. The example serves a
simple page which has some JavaScript in it to create a websocket to a
server, and send and receive data from it.

The server just echoes back any data is receives on the websocket.

To run the server:

    vertx run websockets/web-socket.clj

Then point your browser at: http://localhost:8080

## Route Match

This example shows how a route matcher can be used with a vert.x HTTP
server to allow REST-style resource based matching of URIS in the
manner of express (JS) or Sinatra.

To run the example:

    vertx run route_match/router.clj

Then point your browser at: http://localhost:8080.

An index page will be served which contains some links to urls of the
form:

/details/<user>/<id>

The server will extract the user and id from the uri and display it on
the returned page.

## SockJS

A simple example demonstrating SockJS connections from a browser. The
example serves a simple page which has some JavaScript in it to create
a SockJS connection to a server, and send and receive data from it.

To run the server:

    vertx run sockjs/sockjs.clj

Then point your browser at: http://localhost:8080

## Eventbus Bridge

This example shows how the vert.x event bus can extend to client side
JavaScript.

To run the server:

    vertx run eventbusbridge/bridge-server.clj

The example shows a simple publish / subscribe client side JavaScript
application that uses the vert.x event bus.

Using the application you can subscribe to one or more "addresses",
then send messages to those addresses.

To run it, open one or more browsers and point them to
http://localhost:8080.

First connect, then try subscribing and sending messages and see how
the separate browsers can interoperate on the event bus.

## EventBus Point to Point

A simple point to point event bus example.

receiver.clj registers an event bus handler that displays a message
when a message is received and replies.

sender.clj sends a message every second, and prints any replies it
receives.

    vertx run eventbus_pointtopoint/receiver.clj -cluster

And in a different console:

    vertx run eventbus_pointtopoint/sender.clj -cluster


## EventBus Pub Sub

A simple publish subscribe event bus example.

receiver.clj registers an event bus handler and displays a message when
some news is received

sender.clj publishes some news every second.

You can start a few receivers

    vertx run eventbus_pubsub/receiver.clj -cluster
    vertx run eventbus_pubsub/receiver.clj -cluster
    vertx run eventbus_pubsub/receiver.clj -cluster

    vertx run eventbus_pubsub/sender.clj -cluster


## Embedded Vert.x 

Demonstrates using an embedded Vertx instance from Clojure. 

Run with:

    cd embedded
    lein run

## Core.async in Vert.x

Demonstrates using Clojure's core.async in Vertx.
Learn more about core.async at: <https://github.com/clojure/core.async>

Run with:

    cd async
    lein run
