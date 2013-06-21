var vertx = require('vertx')
var console = require('vertx/console')

vertx.createHttpClient().port(8080).getNow('/', function(resp) {
  console.log("Got response " + resp.statusCode());
  resp.bodyHandler(function(body) {
    console.log("Got data " + body);
  })
});
