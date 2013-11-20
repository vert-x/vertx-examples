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

import java.nio.file.Files
import java.nio.file.Paths

val client = vertx.createHttpClient.setPort(8080).setHost("localhost")

    val req = client.put("/some-url", { response: HttpClientResponse =>
        container.logger.info("File uploaded " + response.statusCode())
    })

    val filename = "upload/upload.txt"

    // For a non-chunked upload you need to specify size of upload up-front

      req.headers().set("Content-Length", String.valueOf(Files.size(Paths.get(filename))))

      // For a chunked upload you don't need to specify size, just do:
      // req.setChunked(true);

      vertx.fileSystem.open(filename, { ar: AsyncResult[AsyncFile] =>
          val file = ar.result()
          val pump = Pump.createPump(file, req)
          pump.start()

          file.endHandler({
              file.close({ ar: AsyncResult[Void] =>
                  if (ar.succeeded()) {
                    req.end()
                    container.logger.info("Sent request")
                  } else {
                    ar.cause().printStackTrace(System.err)
                  }
              })
          })
         
      })