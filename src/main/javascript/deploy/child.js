var container = require('vertx/container')

var log = container.logger;

log.info("in child.js, config is " + container.config);

log.info(JSON.stringify(container.config));
