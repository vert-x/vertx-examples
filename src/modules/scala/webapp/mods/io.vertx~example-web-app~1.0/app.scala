/*
This verticle contains the configuration for our application and co-ordinates
start-up of the verticles that make up the application.
*/

// Our application config - you can maintain it here or alternatively you could
// stick it in a conf.json text file and specify that on the command line when
// starting this verticle

// Configuration for the web server
val webServerConf = Json.obj(

  // Normal web server stuff

  "port" -> 8080,
  "host" -> "localhost",
  "ssl" -> true,

  // Configuration for the event bus client side bridge
  // This bridges messages from the client side to the server side event bus
  "bridge" -> true,

  // This defines which messages from the client we will let through
  // to the server side
  "inbound_permitted" -> Json.arr(
    // Allow calls to login and authorise
    Json.obj(
      "address" -> "vertx.basicauthmanager.login"
    ),
    // Allow calls to get static album data from the persistor
    Json.obj(
      "address" -> "vertx.mongopersistor",
      "match" -> Json.obj(
        "action" -> "find",
        "collection" -> "albums"
      )
    ),
    // And to place orders
    Json.obj(
      "address" -> "vertx.mongopersistor",
      "requires_auth" -> true,  // User must be logged in to send let these through
      "match" -> Json.obj(
        "action" -> "save",
        "collection" -> "orders"
      )
    )
  ),

  // This defines which messages from the server we will let through to the client
  "outbound_permitted" -> Json.arr(Json.obj())
    
)

// Now we deploy the modules that we need

// Deploy a MongoDB persistor module

container.deployModule("io.vertx~mod-mongo-persistor~2.0.0-final", Json.obj(), 1, { err: AsyncResult[String] =>
  // And when it's deployed run a script to load it with some reference
  // data for the demo
  if (err.succeeded()) {
    container.logger.info("hmm")
    initialize()
  } else {
    err.cause().printStackTrace()
  }
})

// Deploy an auth manager to handle the authentication

container.deployModule("io.vertx~mod-auth-mgr~2.0.0-final")

// Start the web server, with the config we defined above

container.deployModule("io.vertx~mod-web-server~2.0.0-final", webServerConf)


def initialize() {
val eb = vertx.eventBus
val pa = "vertx.mongopersistor"

val albums = Json.arr(
  Json.obj(
    "artist" -> "The Wurzels",
    "genre" -> "Scrumpy and Western",
    "title" -> "I Am A Cider Drinker",
    "price" -> 0.99
  ),
  Json.obj(
    "artist" -> "Vanilla Ice",
    "genre" -> "Hip Hop",
    "title" -> "Ice Ice Baby",
    "price" -> 0.01
  ),
  Json.obj(
    "artist" -> "Ena Baga",
    "genre" -> "Easy Listening",
    "title" -> "The Happy Hammond",
    "price" -> 0.50
  ),
  Json.obj(
    "artist" -> "The Tweets",
    "genre" -> "Bird related songs",
    "title" -> "The Birdy Song",
    "price" -> 1.20
  )
)

// First delete everything

eb.send(pa, Json.obj("action" -> "delete", "collection" -> "albums", "matcher" -> Json.obj()), { reply: Message[_] =>
  eb.send(pa, Json.obj("action" -> "delete", "collection" -> "users", "matcher" -> Json.obj()), { reply: Message[_] =>
    // Insert albums - in real life price would probably be stored in a different collection, but, hey, this is a demo.

    for (i <- 0 until albums.size()) {
      eb.send(pa, Json.obj(
        "action" -> "save",
        "collection" -> "albums",
        "document" -> albums.get(i)
      ))
    }

    // And a user

    eb.send(pa, Json.obj(
      "action" -> "save",
      "collection" -> "users",
      "document" -> Json.obj(
        "firstname" -> "Tim",
        "lastname" -> "Fox",
        "email" -> "tim@localhost.com",
        "username" -> "tim",
        "password" -> "password"
      )
    ))

  })
})
}