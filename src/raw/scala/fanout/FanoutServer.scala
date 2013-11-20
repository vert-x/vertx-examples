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

import java.util.{ Set => JSet }
import scala.collection.JavaConversions._

val connections: JSet[String] = vertx.sharedData.getSet("conns")

vertx.createNetServer.connectHandler({ socket: NetSocket =>
  connections.add(socket.writeHandlerID)

  socket.dataHandler({ buffer: Buffer =>
    for (actorID <- connections) {
      vertx.eventBus.publish(actorID, buffer)
    }
  })

  socket.closeHandler({
    connections.remove(socket.writeHandlerID())
  })
}).listen(8080)