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
    vertx.createHttpServer().requestHandler {
      void onRequest(HttpServerRequest req) {
        print("Got request ``req.uri``");
        print("Headers are: ");
        for (header in req.headers) {
          print("``header.key``:``header.item``");
        }
        req.response.headers {
          "Content-Type"->"text/html; charset=UTF-8"
        };
        req.response.end("<html><body><h1>Hello from vert.x!</h1></body></html>");
      }
    }.listen(8080);
  }
}
