var vertx = require('vertx')
var console = require('vertx/console')

var client = vertx.createHttpClient().port(8080);

client.connectWebsocket('/some-uri', function(websocket) {
  websocket.dataHandler(function(data) {
    console.log('Received data ' + data);
    client.close();
  });
  websocket.writeTextFrame('Hello world');
});
