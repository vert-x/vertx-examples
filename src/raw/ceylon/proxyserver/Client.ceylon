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
import org.vertx.java.core.buffer {
  Buffer
}
shared class Client() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    value req = vertx.createHttpClient {
      port = 8080;
      host = "localhost";
    }.put("/some-url");
    req.response.onComplete(void(HttpClientResponse resp) {
        resp.stream.dataHandler(void(Buffer buffer) {
            print("Got response data:``buffer``");
          });
      });
    //Write a few chunks
    req.chunked = true;
    for (i in 0..10) {
      req.write("client-data-chunk-``i``");
    }
    req.end();
  }
}
