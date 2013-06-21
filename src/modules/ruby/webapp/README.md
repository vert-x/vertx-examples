# "Real-time" Web Application Example

This is a full end-end "real-time" web appplication which has a modern JavaScript client side MVVM application that communicates via the event bus with a persistor.

It's the same application from the tutorial.

You will need an instance of MongoDB server (with default settings) running on localhost.

To run:

    vertx runmod io.vertx~example-web-app~1.0

To see log output set the following in `conf/logging.properties` in the Vert.x install directory.

    org.vertx.level=FINE

Then point your browser at https://localhost:8080 and start shopping! (Note it's https not http!)

The example uses knockout.js at the client side to render the application. Vert.x is completely agnostic as to what
client side toolkit you use - we have just chosen this for this example.

The example uses the Vert.x event bus to extend into client side JavaScript so the browser can communicate with
server side components.