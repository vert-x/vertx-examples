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
shared class ServerExample() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    value server = vertx.createHttpServer().requestHandler(void(HttpServerRequest req) {
        print("Got request: ``req.uri``");
        print("Headers are: ");
        for (header in req.headers) {
          print("``header.key``:``header.item``");
        }
        req.response.headers { "Content-Type"->"text/html; charset=UTF-8" };
        req.response.chunked = true;
        req.response.write(["<html><body><h1>Hello from vert.x!</h1></body></html>", "UTF-8"]).end();
      });
    server.ssl = true;
    server.keyStorePath = "server-keystore.jks";
    server.keyStorePassword = "wibble";
    server.listen(4443);
  }
}
