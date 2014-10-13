import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.http {
  HttpServerRequest,
  HttpServerFileUpload
}
shared class SimpleFormUploadServer() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    vertx.createHttpServer().requestHandler {
      void onRequest(HttpServerRequest req) {
        if (req.uri == "/") {
          req.response.sendFile("simpleformupload/index.html");
        } else if (req.uri.startsWith("/form")) {
          req.expectMultiPart(true);
          req.uploadHandler(
            void(HttpServerFileUpload upload) {
              upload.readStream.exceptionHandler(void(Throwable t) {
                  req.response.end("Upload failed");
                });
              upload.readStream.endHandler(void() {
                  req.response.end("Upload successful, you should see the file in the server directory");
                });
              upload.streamToFileSystem(upload.filename);
            }
          );
        } else {
          req.response.status(404).end();
        }
      }
    }.listen(8080);
  }
}
