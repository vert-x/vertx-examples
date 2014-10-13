import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import ceylon.promise {
  Promise
}
import io.vertx.ceylon.core {
  Vertx
}

shared class Starter() extends Verticle() {
  
  shared actual Promise<Anything> asyncStart(Vertx vertx, Container container) {
    return container.deployVerticle("foo.js");
  }
}
