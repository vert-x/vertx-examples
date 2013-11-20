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

import java.util.UUID

vertx.createHttpServer().requestHandler({ req: HttpServerRequest =>

  // We first pause the request so we don't receive any data between now and when the file is opened
  req.pause()

  val filename = "upload/file-" + UUID.randomUUID().toString() + ".upload"

  vertx.fileSystem.open(filename, { ar: AsyncResult[AsyncFile] =>
    if (ar.failed()) {
      ar.cause().printStackTrace()
    } else {
      val file = ar.result()
      val pump = Pump.createPump(req, file)
      val start = System.currentTimeMillis
      req.endHandler({
        file.close({ ar: AsyncResult[Void] =>
          if (ar.succeeded()) {
            req.response().end()
            val end = System.currentTimeMillis
            container.logger.info("Uploaded " + pump.bytesPumped() + " bytes to " + filename + " in " + (end - start) + " ms")
          } else {
            ar.cause().printStackTrace(System.err)
          }
        })
      })
      pump.start()
      req.resume()
    }
  })
}).listen(8080)
