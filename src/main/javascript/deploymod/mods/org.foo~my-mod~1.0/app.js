var container = require('vertx/container');
var console = require('vertx/console');

console.log("in MyMod!")
console.log("some-var is " + container.config['some-var'])
