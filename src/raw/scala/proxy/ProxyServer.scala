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

val client = vertx.createHttpClient.setHost("localhost").setPort(8080)

vertx.createHttpServer.requestHandler({ req: HttpServerRequest =>
  container.logger.info("Proxying request: " + req.uri())
  val cReq = client.request(req.method(), req.uri(), { cRes: HttpClientResponse =>
    container.logger.info("Proxying response: " + cRes.statusCode())
    req.response().setStatusCode(cRes.statusCode())
    req.response().headers().set(cRes.headers())
    req.response().setChunked(true)
    cRes.dataHandler({ data: Buffer =>
      container.logger.info("Proxying response body:" + data)
      req.response().write(data)
    })
    cRes.endHandler({
      req.response().end()
    })
  })
  cReq.headers().set(req.headers())
  cReq.setChunked(true)
  req.dataHandler({ data: Buffer =>
    container.logger.info("Proxying request body:" + data)
    cReq.write(data)
  })
  req.endHandler({
    container.logger.info("end of the request")
    cReq.end()
  })
}).listen(8080)