package websockets;

/*
 * Copyright 2013 the original author or authors.
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

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.WebSocket
import org.vertx.scala.core.buffer.Buffer

class WebsocketsClient extends Verticle {

  override def start() {
    // Setting host as localhost is not strictly necessary as it's the default
    vertx.createHttpClient().setHost("localhost").setPort(8080).connectWebsocket("/myapp", { websocket: WebSocket =>
      websocket.dataHandler({ data: Buffer =>
        println("Received " + data)
      })
      //Send some data
      websocket.writeTextFrame("hello world")
    })
  }
}

