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
import org.vertx.java.core.buffer {
  Buffer
}

shared class SSLClient() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    value client = vertx.createNetClient();
    client.ssl = true;
    client.trustAll = true;
    client.connect(1234).onComplete(void(NetSocket sock) {
        sock.readStream.dataHandler(void(Buffer buffer) { print("Net client receiving: ``buffer``"); });
        for (i in 0..10) {
          value str = "hello``i``\n";
          print("Net client sending: ``str``");
          sock.write(Buffer(str));
        }
      });
  }
}
