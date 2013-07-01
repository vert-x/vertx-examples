vertx = require "vertx"
console = require "vertx/console"

client = vertx.createNetClient()
client.connect 1234, (err, sock) ->

  sock.dataHandler (buffer) ->
    console.log "client receiving " + buffer.toString()

  i = 0

  while i < 10
    str = "hello" + i + "\n"
    console.log "Net client sending: " + str
    sock.write new vertx.Buffer(str)
    i++