import io.vertx.ceylon.platform {
  Verticle,
  Container
}
import io.vertx.ceylon.core {
  Vertx
}
import io.vertx.ceylon.core.http {
  RouteMatcher,
  HttpServerRequest
}

String safe([String+]? s) {
  if (exists s) {
    return s[0];
  } else {
    return "";
  }
}

shared class RouteMatchExample() extends Verticle() {
  
  shared actual void start(Vertx vertx, Container container) {
    
    value rm = RouteMatcher();
    rm.get("/details/:user/:id", (HttpServerRequest req) => req.response.end("User: ``safe(req.params["user"])`` ID: ``safe(req.params["id"])``"));
    
    // Catch all - serve the index page
    rm.getWithRegEx(".*", (HttpServerRequest req) => req.response.end(
          """<html>
             <head><title>Route Matcher example</title></head>
             <body>
               Click on these links:<br><br>
               <a href="/details/joe/123">Joe</a>  <br>
               <a href="/details/bob/765">Bob</a>  <br>
               <a href="/details/jeff/243">Jeff</a> <br>
               <a href="/details/jane/243">Jane</a> <br>
             </body>
             </html>"""));
    
    vertx.createHttpServer().requestHandler(rm.handle).listen(8080);
  }
}
