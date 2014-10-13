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
import org.vertx.java.core.buffer {
  Buffer
}
shared class Server() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    vertx.createHttpServer().requestHandler(void(HttpServerRequest req) {
        print("Got request: ``req.uri``");
        print("Headers are: ");
        for (header in req.headers) {
          print("``header.key``: ``header.item``");
        }
        req.stream.dataHandler(void(Buffer buffer) {
            print("Got data: ``buffer``");
          });
        req.stream.endHandler(void() {
            req.response.chunked = true;
            for (i in 0..10) {
              req.response.write("server-data-chunk-``i``");
            }
            req.response.end();
          });
      }).listen(8282);
  }
}
