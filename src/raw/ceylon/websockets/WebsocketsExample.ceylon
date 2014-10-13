import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.http {
  ServerWebSocket,
  HttpServerRequest
}
import org.vertx.java.core.buffer {
  Buffer
}
shared class WebsocketsExample() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.createHttpServer().websocketHandler(void(ServerWebSocket websocket) {
        if (websocket.path == "/myapp") {
          websocket.readStream.dataHandler(
            (Buffer buffer) => websocket.writeTextFrame(buffer.string)); // Echo it back
        } else {
          websocket.reject();
        }
      }).requestHandler(void(HttpServerRequest req) {
        if (req.path == "/") {
          req.response.sendFile("websockets/ws.html"); // Serve the html
        }
      }).listen(8080);
  }
}
