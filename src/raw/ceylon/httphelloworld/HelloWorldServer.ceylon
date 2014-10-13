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
shared class HelloWorldServer() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.createHttpServer().requestHandler(void(HttpServerRequest req) {
        req.response.headers { "Content-Type"->"text/plain" };
        req.response.end("Hello World");
      }).listen(8080);
  }
}
