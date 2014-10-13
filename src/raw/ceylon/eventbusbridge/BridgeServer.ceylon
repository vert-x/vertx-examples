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
import ceylon.json {
  Array,
  Object
}
import ceylon.promise {
  Deferred
}
import io.vertx.ceylon.core.sockjs {
  EventBusBridgeHook,
  SockJSSocket
}
shared class BridgeServer() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    value server = vertx.createHttpServer();
    
    // Also serve the static resources. In real life this would probably be done by a CDN
    server.requestHandler(void(HttpServerRequest req) {
        if (req.path == "/") {
          req.response.sendFile("eventbusbridge/index.html"); // Serve the index.html
        } else if (req.path == "/vertxbus.js") {
          req.response.sendFile("eventbusbridge/vertxbus.js"); // Serve the js
        }
      });
    
    value permitted = Array();
    permitted.add(Object()); // Let everything through
    
    value hook = ServerHook();
    value sockJSServer = vertx.createSockJSServer(server);
    sockJSServer.setHook(hook);
    sockJSServer.bridge(Object { "prefix"->"/eventbus" }, permitted, permitted);
    
    server.listen(8080);
  }
  
  class ServerHook() satisfies EventBusBridgeHook {
    
    shared actual Boolean handleSocketCreated(SockJSSocket sock) {
      print("headers ``sock.headers``");
      value origin = sock.headers["origin"];
      if (exists origin, origin.first == "http://localhost:8080") {
        print("Origin is ``origin.first``");
        return true;
      } else {
        return false;
      }
    }
    
    shared actual void handleSocketClosed(SockJSSocket sock) {
      print("handleSocketClosed, sock = ``sock``");
    }
    
    shared actual Boolean handleSendOrPub(SockJSSocket sock, Boolean send, Object msg, String address) {
      print("handleSendOrPub, sock = ``sock``, send = ``send``, address = ``address``");
      print(msg);
      return true;
    }
    
    shared actual Boolean handlePreRegister(SockJSSocket sock, String address) {
      print("handlePreRegister, sock = ``sock``, address = ``address``");
      return true;
    }
    
    shared actual void handlePostRegister(SockJSSocket sock, String address) {
      print("handlePostRegister, sock = ``sock``, address = ``sock``");
    }
    
    shared actual Boolean handleUnregister(SockJSSocket sock, String address) {
      print("handleUnregister, sock = ``sock``, address = ``address``");
      return true;
    }
    
    shared actual Boolean handleAuthorise(Object message, String sessionID, Deferred<Boolean> handler) {
      return false;
    }
  }
}
