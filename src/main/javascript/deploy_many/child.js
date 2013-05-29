var console = require('vertx/console')

console.log("in child")

function vertxStop() {
  console.log("child stopped")
}
