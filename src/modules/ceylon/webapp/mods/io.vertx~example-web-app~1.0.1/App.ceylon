import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import ceylon.json { JsonObject = Object, JsonArray = Array }

shared class App() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    // Our application config - you can maintain it here or alternatively you could
    // stick it in a conf.json text file and specify that on the command line when
    // starting this verticle
    
    // Configuration for the web server
    value webServerConf = JsonObject {
      
      // Normal web server stuff
      "port"->8080,
      "host"->"localhost",
      "ssl"->true,
      
      // Configuration for the event bus client side bridge
      // This bridges messages from the client side to the server side event bus
      "bridge"->true,
      
      // This defines which messages from the client we will let through
      // to the server side
      "inbound_permitted"->JsonArray {
        // Allow calls to login
        JsonObject {
          "address"->"vertx.basicauthmanager.login"
        },
        // Allow calls to get static album data from the persistor
        JsonObject {
          "address"->"vertx.mongopersistor",
          "match"->JsonObject {
            "action"->"find",
            "collection"->"albums"
          }
        },
        // And to place orders
        JsonObject {
          "address"->"vertx.mongopersistor",
          "requires_auth"->true,
          "match"->JsonObject {
            "action"->"save",
            "collection"->"orders"
          }
        }
      },
      
      // This defines which messages from the server we will let through to the client
      "outbound_permitted"->JsonArray {
        JsonObject { }
      }
    };

    // Now we deploy the modules that we need
    
    // Deploy a MongoDB persistor module
    
    value deployed = container.deployModule("io.vertx~mod-mongo-persistor~2.0.0-final");
    deployed.onComplete {
      void onFulfilled(String id) {
        container.deployVerticle("StaticData.ceylon");
      }
      void onRejected(Throwable cause) {
        print("Failed to deploy:");
        cause.printStackTrace();
      }
    };

    // Deploy an auth manager to handle the authentication

    container.deployModule("io.vertx~mod-auth-mgr~2.0.0-final");
    
    // Start the web server, with the config we defined above
    
    container.deployModule("io.vertx~mod-web-server~2.0.0-final", webServerConf).onComplete {
      void onFulfilled(String id) {
        print("deployed webserver");
      }
      void onRejected(Throwable cause) {
        print("could not deploy webserver");
        cause.printStackTrace();
      }
    };
  }  
}