var vertx = require('vertx')
var console = require('vertx/console')

vertx.createHttpServer().requestHandler(function(req) {
  console.log("Got request " + req.uri());

  var hdrs = req.headers();
  hdrs.forEach(function(k, v) {
    console.log(k + ":" + v)
  });

  req.dataHandler(function(data) { console.log("Got data " + data) });

  req.endHandler(function() {
    // Now send back a response
    req.response.chunked(true)

    for (var i = 0; i < 10; i++) {
      req.response.write("server-data-chunk-" + i);
    }

    req.response.end();
  });
}).listen(8282)
