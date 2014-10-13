import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.http {
  HttpServerRequest
}
import io.vertx.ceylon.core.sockjs {
  SockJSSocket
}
import ceylon.json {
  Object
}

shared class SockJSExample() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    value server = vertx.createHttpServer();
    
    server.requestHandler(void(HttpServerRequest req) {
        if (req.path == "/") {
          req.response.sendFile("sockjs/index.html"); // Serve the html
        }
      });
    
    value sockServer = vertx.createSockJSServer(server);
    
    sockServer.installApp(Object { "prefix"->"/testapp" }, void(SockJSSocket sock) {
        sock.readStream.dataHandler(sock.writeStream.write); // Echo it back
      });
    
    server.listen(8080);
  }
}
