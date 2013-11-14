package proxy;

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
import org.vertx.scala.core.http.HttpServerRequest
import org.vertx.scala.core.buffer.Buffer
import scala.collection.JavaConversions._

class Server extends Verticle {

  override def start() {
    vertx.createHttpServer().requestHandler({ req: HttpServerRequest =>
      println("Got request: " + req.uri())
      println("Headers are: ")

      for (entry <- req.headers.entries()) {
        println(entry.getKey() + ":" + entry.getValue())
      }
      req.dataHandler({ data: Buffer =>
        println("Got data: " + data)
      })
      req.endHandler({
        req.response().setChunked(true)
        //Now we got everything, send back some data
        (0 until 10) map { i => req.response.write("server-data-chunk" + i) }
        req.response().end()
      })
    }).listen(8282)
  }
}
