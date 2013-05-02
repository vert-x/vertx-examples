var vertx = require('vertx')

vertx.createNetServer().connectHandler(function(sock) {
  new vertx.Pump(sock, sock).start();
}).listen(1234);
