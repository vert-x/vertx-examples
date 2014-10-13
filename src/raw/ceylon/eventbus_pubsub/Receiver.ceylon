import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.eventbus {
  Message
}
shared class Receiver() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    vertx.eventBus.registerHandler {
      address = "news-feed";
      void onMessage(Message<String> msg) {
        print("Received news: ``msg.body``");
      }
    };
  }
}
