var vertx = require('vertx')
var console = require('vertx/console')

var client = vertx.createHttpClient().port(8080);
var request = client.put('/', function(resp) {
  console.log("Got response " + resp.statusCode());
  resp.bodyHandler(function(body) {
    console.log("Got data " + body);
  })
});

request.chunked(true)

for (var i = 0; i < 10; i++) {
  request.write("client-chunk-" + i);
}
request.end();
