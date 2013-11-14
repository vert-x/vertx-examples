package route_match;

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
import org.vertx.scala.core.http.RouteMatcher
import org.vertx.scala.core.http.HttpServerRequest

class RouteMatchExample extends Verticle {

  override def start() {

    val rm = new RouteMatcher()

    rm.get("/details/:user/:id", { req: HttpServerRequest =>
      req.response().end("User: " + req.params().get("user") + " ID: " + req.params().get("id"))
    })

    // Catch all - serve the index page
    rm.getWithRegEx(".*", { req: HttpServerRequest =>
      req.response().sendFile("route_match/index.html")
    })

    vertx.createHttpServer().requestHandler(rm).listen(8080)
  }
}
