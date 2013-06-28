var container = require('vertx/container')
var console = require('vertx/console')

console.log("in MyMod!")

// Display the value from the config
console.log("some-var is " + container.config['some-var'])
