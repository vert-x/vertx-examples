var vertx = require('vertx');
var console = require('vertx/console');
var bar = require('bar.js');

var server = vertx.createHttpServer();

server.requestHandler(function(request) {
  request.response.end(bar.sayHello("User"));
});

server.listen(8181, 'localhost', function(err) {
  if (!err) {
    console.log("Server listening on 8181");
  }
});
