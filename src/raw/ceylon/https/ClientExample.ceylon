import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.http {
  HttpClientResponse
}

shared class ClientExample() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    value client = vertx.createHttpClient();
    client.ssl = true;
    client.trustAll = true;
    client.port = 4443;
    client.host = "localhost";
    value resp = client.get("/").end().response;
    resp.onComplete((HttpClientResponse resp) =>
        resp.stream.dataHandler(print)
    );
  }
}
