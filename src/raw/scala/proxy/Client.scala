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
import org.vertx.scala.core.http.HttpClientResponse
import org.vertx.scala.core.buffer.Buffer

class Client extends Verticle {

  override def start() {
    val req = vertx.createHttpClient.setPort(8080).setHost("localhost").put("/some-url", { response: HttpClientResponse =>
      response.dataHandler({ data: Buffer =>
        println("Got response data:" + data)
      })
    })
    //Write a few chunks
    req.setChunked(true)
    for (i <- 0 until 10) {
      req.write("client-data-chunk-" + i)
    }
    req.end()
  }
}
