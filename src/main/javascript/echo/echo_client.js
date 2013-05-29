var vertx = require('vertx')
var console = require('vertx/console')

var client = vertx.createNetClient();

client.connect(1234, function(err, sock) {

  sock.dataHandler(function(buffer) {
    console.log("client receiving " + buffer.toString());
  });

  // Now send some data
  for (var i = 0; i < 10; i++) {
    var str = "hello" + i + "\n";
    console.log("Net client sending: " + str);
    sock.write(new vertx.Buffer(str));
  }
});


