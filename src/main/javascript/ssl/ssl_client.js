/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var vertx = require('vertx')
var console = require('vertx/console')

var client = vertx.createNetClient().ssl(true).trustAll(true);

client.connect(1234, function(err, sock) {

  sock.dataHandler(function(buffer) {
    console.log("client receiving " + buffer.toString());
  });

  // Now send some data
  for (var i = 0; i < 10; i++) {
    var str = "hello" + i + "\n";
    console.log("Net client sending: " + str);
    sock.write(new vertx.Buffer(str));
  }
});


