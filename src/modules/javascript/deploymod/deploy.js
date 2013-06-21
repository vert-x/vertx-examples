var container = require('vertx/container');
var console = require('vertx/console');

console.log("Deploying module");

var conf = {"some-var" : "hello"};

container.deployModule("org.foo~my-mod~1.0", conf, 1, function(err, deploymentID) {
  if (err) {
    console.log("Failed to deploy: " + err);
  } else {
    console.log("This gets called when deployment is complete, deployment id is " + deploymentID);
  }
});
