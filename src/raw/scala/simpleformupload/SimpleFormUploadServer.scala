package simpleformupload;

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
import org.vertx.scala.core.http.HttpServerFileUpload
import org.vertx.scala.core.Handler

class SimpleFormUploadServer extends Verticle {
  override def start() {
    vertx.createHttpServer.requestHandler({ req: HttpServerRequest =>
      if (req.uri().equals("/")) {
        // Serve the index page
        req.response().sendFile("simpleformupload/index.html")
      } else if (req.uri().startsWith("/form")) {
        req.expectMultiPart(true)
        req.uploadHandler({ upload: HttpServerFileUpload =>
          upload.exceptionHandler({ event: Handler[Throwable] =>
            req.response().end("Upload failed")
          }.asInstanceOf[Handler[Throwable]])
          upload.endHandler({ event: Handler[Void] =>
            req.response().end("Upload successful, you should see the file in the server directory")
          }.asInstanceOf[Handler[Void]])
          upload.streamToFileSystem(upload.filename())
        }.asInstanceOf[Handler[HttpServerFileUpload]])
      } else {
        req.response().setStatusCode(404)
        req.response().end()
      }
    }).listen(8080)
  }
}
