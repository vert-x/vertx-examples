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
shared class EchoClient() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.createNetClient().connect(1234, "localhost").onComplete(
      void(NetSocket sock) {
        sock.readStream.dataHandler(void(Buffer buffer) {
            print("Net client receiving: ``buffer``");
          });
        for (i in 0..10) {
          value str = "hello``i``\n";
          print("Net client sending: ``str``");
          sock.write(Buffer(str));
        }
      },
      void(Throwable t) {
        t.printStackTrace();
      }
    );
  }
}
