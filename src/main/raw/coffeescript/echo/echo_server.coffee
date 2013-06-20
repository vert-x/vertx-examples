vertx = require "vertx"

vertx.createNetServer().connectHandler((sock) ->
  new vertx.Pump(sock, sock).start()
).listen 1234