//Test if requiring bar lib works locally
var bar = require("./bar.js");

if (bar.sayHello("User") == "Hello User") {
  console.log("requiring bar.js works");
} else {
  console.log("requiring bar.js didn't work");
}
