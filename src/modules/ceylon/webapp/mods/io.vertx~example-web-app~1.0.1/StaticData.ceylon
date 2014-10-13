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
import ceylon.json {
  JsonObject=Object,
  JsonArray=Array
}
shared class StaticData() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {

    value eb = vertx.eventBus;
    value pa = "vertx.mongopersistor";
    
    value albums = JsonArray {
      JsonObject {
        "artist" -> "The Wurzels",
        "genre" -> "Scrumpy and Western",
        "title" -> "I Am A Cider Drinker",
        "price" -> 0.99
      },
      JsonObject {
        "artist" -> "Vanilla Ice",
        "genre" -> "Hip Hop",
        "title" -> "Ice Ice Baby",
        "price" -> 0.01
      },
      JsonObject {
        "artist" -> "Ena Baga",
        "genre" -> "Easy Listening",
        "title" -> "The Happy Hammond",
        "price" -> 0.50
      },
      JsonObject {
        "artist" -> "The Tweets",
        "genre" -> "Bird related songs",
        "title" -> "The Birdy Song",
        "price" -> 1.20
      }
    };
    
    // First delete albums
    eb.send<JsonObject>(pa, JsonObject { "action"->"delete", "collection"->"albums", "matcher"->JsonObject()}).onComplete {
      void onFulfilled(Message<JsonObject> repl) {
        for (album in albums) {
          eb.send(pa, JsonObject {
            "action"->"save",
            "collection"->"albums",
            "document"->album
          });
        }
      }
    };
    
    // Delete users
    eb.send<JsonObject>(pa, JsonObject { "action"->"delete", "collection"->"users", "matcher"->JsonObject()}).onComplete {
      void onFulfilled(Message<JsonObject> repl) {
        for (album in albums) {
          eb.send(pa, JsonObject {
            "action"->"save",
            "collection"->"users",
            "document"->JsonObject {
              "firstname"->"Tim",
              "lastname"->"Fox",
              "email"->"tim@localhost.com",
              "username"->"tim",
              "password"->"password"
            }
          });
        }
      }
    };
  }  
}