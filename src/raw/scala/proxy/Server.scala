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

import scala.collection.JavaConversions._

vertx.createHttpServer().requestHandler({ req: HttpServerRequest =>
  container.logger.info("Got request: " + req.uri())
  container.logger.info("Headers are: ")

  for (entry <- req.headers.entries()) {
    container.logger.info(entry.getKey() + ":" + entry.getValue())
  }
  req.dataHandler({ data: Buffer =>
    container.logger.info("Got data: " + data)
  })
  req.endHandler({
    req.response().setChunked(true)
    //Now we got everything, send back some data
    (0 until 10) map { i => req.response.write("server-data-chunk" + i) }
    req.response().end()
  })
}).listen(8080)
