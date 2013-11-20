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

import scala.collection.JavaConversions._
import java.util.{ Set => JSet }
import org.vertx.scala.core.parsetools.RecordParser

vertx.createNetServer().connectHandler({ socket: NetSocket =>
  socket.dataHandler({ x: Buffer =>
    container.logger.info("x " + x.toString())
    RecordParser.newDelimited("\n", { frame: Buffer =>
      val line = frame.toString().trim()
      container.logger.info("Line is " + line)
      val parts = line.split("\\,")

      if (line.startsWith("subscribe")) {
        container.logger.info("Topic is " + parts(1))
        val set: JSet[String] = vertx.sharedData.getSet(parts(1))
        set.add(socket.writeHandlerID())
      } else if (line.startsWith("unsubscribe")) {
        vertx.sharedData.getSet(parts(1)).remove(socket.writeHandlerID())
      } else if (line.startsWith("publish")) {
        container.logger.info("Publish to topic is " + parts(1))
        val actorIDs: JSet[String] = vertx.sharedData.getSet(parts(1))
        for (actorID <- actorIDs) {
          container.logger.info("Sending to verticle")
          vertx.eventBus.publish(actorID, Buffer(parts(2)))
        }
      }
      ()
    }).handle(x.toJava)
  })
}).listen(8080)