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
shared class SimpleFormServer() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    vertx.createHttpServer().requestHandler(void(HttpServerRequest req) {
        
        if (req.uri == "/") {
          req.response.sendFile("simpleform/index.html");
        } else if (req.uri.startsWith("/form")) {
          req.response.chunked = true;
          req.expectMultiPart(true);
          req.formAttributes.onComplete(void(Map<String,{String+}> form) {
              for (entry in form) {
                req.response.write("Got attr ``entry.key`` : ``entry.item``\n");
              }
              req.response.end();
            }, (Throwable t) => t.printStackTrace());
        } else {
          req.response.status(404);
          req.response.end();
        }
      }).listen(8080);
  }
}
