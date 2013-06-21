var container = require('container')
var console = require('console')

console.log("in MyMod!")

// Display the value from the config
console.log("some-var is " + container.config['some-var'])
