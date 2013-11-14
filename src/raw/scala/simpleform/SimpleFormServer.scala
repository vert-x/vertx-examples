package simpleform;

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
import scala.collection.JavaConversions._

class SimpleFormServer extends Verticle {

  override def start() {
    vertx.createHttpServer.requestHandler({ req: HttpServerRequest =>
      if (req.uri().equals("/")) {
        // Serve the index page
        req.response().sendFile("simpleform/index.html")
      } else if (req.uri().startsWith("/form")) {
        req.response().setChunked(true)
        req.expectMultiPart(true)
        req.endHandler({
          for (entry <- req.formAttributes()) {
            req.response().write("Got attr " + entry.getKey() + " : " + entry.getValue() + "\n")
          }
          req.response().end()
        })
      } else {
        req.response().setStatusCode(404)
        req.response().end()
      }
    }).listen(8080)
  }
}