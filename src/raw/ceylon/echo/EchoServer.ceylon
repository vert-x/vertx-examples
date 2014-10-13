import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.net {
  NetSocket
}
import io.vertx.ceylon.core.stream {
  Pump
}

shared class EchoServer() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.createNetServer().connectHandler(void(NetSocket sock) {
        Pump(sock.readStream, sock.writeStream).start();
      }).listen(1234);
  }
}
