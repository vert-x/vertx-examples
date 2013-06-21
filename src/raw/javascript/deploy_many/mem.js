var container = require('vertx/container');
var console = require('vertx/console');

function deploy_it(count) {
  container.deployVerticle('child.js', function(deploy_id) {
    console.log("deployed " + count);
    undeploy_it(deploy_id, count);
  });
}

function undeploy_it(deploy_id, count) {
  container.undeployVerticle(deploy_id, function() {
    count++;
    if (count < 10) {
      deploy_it(count);
    } else {
      console.log("done!");
    }
  });
}

deploy_it(0) ;
