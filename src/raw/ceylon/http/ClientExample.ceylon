import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.http {
  HttpClientResponse,
  textBody
}
shared class ClientExample() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.createHttpClient {
      port = 8080;
      host = "localhost";
    }.get("/").end().response.compose<String>((HttpClientResponse resp) => resp.parseBody(textBody)).onComplete(print);
  }
}
