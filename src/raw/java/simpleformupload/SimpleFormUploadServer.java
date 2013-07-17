/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package simpleformupload;


import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerFileUpload;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;


/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class SimpleFormUploadServer extends Verticle {
  public void start() {
    vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
      public void handle(final HttpServerRequest req) {
        if (req.uri().equals("/")) {
          // Serve the index page
          req.response().sendFile("simpleformupload/index.html");
        } else if (req.uri().startsWith("/form")) {
          req.expectMultiPart(true);
          req.uploadHandler(new Handler<HttpServerFileUpload>() {
            @Override
            public void handle(final HttpServerFileUpload upload) {
              upload.exceptionHandler(new Handler<Throwable>() {
                @Override
                public void handle(Throwable event) {
                  req.response().end("Upload failed");
                }
              });
              upload.endHandler(new Handler<Void>() {
                @Override
                public void handle(Void event) {
                  req.response().end("Upload successful, you should see the file in the server directory");
                }
              });
              upload.streamToFileSystem(upload.filename());
            }
          });
        } else {
          req.response().setStatusCode(404);
          req.response().end();
        }
      }
    }).listen(8080);
  }
}
