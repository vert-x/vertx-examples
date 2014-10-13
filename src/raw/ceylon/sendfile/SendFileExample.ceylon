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
shared class SendFileExample() extends Verticle() {
  
  String webroot = "sendfile/";
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.createHttpServer().requestHandler(void(HttpServerRequest req) {
        if (req.uri == "/") {
          req.response.sendFile("``webroot``index.html");
        } else {
          //Clearly in a real server you would check the path for better security!!
          req.response.sendFile(webroot + req.path);
        }
      }).listen(8080);
  }
}
