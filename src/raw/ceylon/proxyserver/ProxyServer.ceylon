import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.platform {
  Container,
  Verticle
}
import io.vertx.ceylon.core.http {
  HttpServerRequest,
  HttpClientResponse
}
import org.vertx.java.core.buffer {
  Buffer
}
shared class ProxyServer() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    value client = vertx.createHttpClient {
      host = "localhost";
      port = 8282;
    };
    
    vertx.createHttpServer().requestHandler(void(HttpServerRequest req) {
        print("Proxying request:``req.uri``");
        value creq = client.request(req.method, req.uri);
        creq.response.onComplete(void(HttpClientResponse cresp) {
            print("Proxying response:``cresp.statusCode``");
            req.response.status(cresp.statusCode);
            req.response.headers(cresp.headers);
            req.response.chunked = true;
            cresp.stream.dataHandler(void(Buffer buffer) {
                print("Proxying response body:``buffer``");
                req.response.write(buffer);
              });
            cresp.stream.endHandler(void() { req.response.end(); });
          });
        creq.headers(req.headers);
        creq.chunked = true;
        req.stream.dataHandler(void(Buffer buffer) {
            print("Proxying request body:``buffer``");
            creq.write(buffer);
          });
        req.stream.endHandler(void() {
            print("end of the request");
            creq.end();
          });
      }).listen(8080);
  }
}
