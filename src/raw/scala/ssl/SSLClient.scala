package ssl;

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
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.net.NetSocket
import org.vertx.scala.core.buffer.Buffer

class SSLClient extends Verticle {

  override def start() {
    vertx.createNetClient.setSSL(true).setTrustAll(true).connect(1234, "localhost", { asyncResult: AsyncResult[NetSocket] =>
      val socket = asyncResult.result()
      socket.dataHandler({ buffer: Buffer =>
        println("Net client receiving: " + buffer.toString("UTF-8"))
      })
      //Now send some dataHandler
      for (i <- 0 until 10) {
        val str = "hello" + i + "\n"
        print("Net client sending: " + str)
        socket.write(Buffer(str))
      }
    })
  }
}