import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.http {
  WebSocket
}
import org.vertx.java.core.buffer {
  Buffer
}
shared class WebsocketsClient() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.createHttpClient(8080, "localhost").connectWebsocket("/myapp").onComplete(
      void(WebSocket websocket) {
        websocket.readStream.dataHandler(
          (Buffer data) => print("Received data " + data.string));
        //Send some data
        websocket.writeTextFrame("hello world");
      }
    );
  }
}
