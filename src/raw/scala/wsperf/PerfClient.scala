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

import java.util.LinkedList
import java.util.HashSet

// Number of connections to create
val CONNS = 1
var client = vertx.createHttpClient().setPort(8080).setHost("localhost").setMaxPoolSize(CONNS)
var statsCount = 0
val eb = vertx.eventBus
val STR_LENGTH = 8 * 1024
val STATS_BATCH = 1024 * 1024
val BUFF_SIZE = 32 * 1024
var message = ""

def PerfClient() {
  val sb = new StringBuilder(STR_LENGTH)
  for (i <- 0 until STR_LENGTH) {
    sb.append('X')
  }
  message = sb.toString()
}

var connectCount = 0
val websockets = new LinkedList[WebSocket]()
val wss = new HashSet[WebSocket]()

def connect(count: Int) {
  client.connectWebsocket("/echo/websocket", { ws: WebSocket =>
    connectCount += 1

    ws.setWriteQueueMaxSize(BUFF_SIZE)
    ws.dataHandler({ data: Buffer =>
      if (!wss.contains(ws)) {
        wss.add(ws)
        if (wss.size() == CONNS) {
          container.logger.info("Received data on all conns")
        }
      }
      val len = data.length()
      statsCount += len
      if (statsCount > STATS_BATCH) {
        eb.send("rate-counter", statsCount)
        statsCount = 0
      }
    })

    websockets.add(ws)
    if (connectCount == CONNS) {
      startWebSocket()
    }
  })

  if (count + 1 < CONNS) {
    vertx.runOnContext({
      connect(count + 1)
    })
  }
}

def startWebSocket() {
  val ws = websockets.poll()
  writeWebSocket(ws)
  if (!websockets.isEmpty()) {
    vertx.runOnContext({
      startWebSocket()
    })
  }
}

client.setReceiveBufferSize(BUFF_SIZE)
client.setSendBufferSize(BUFF_SIZE)
client.setConnectTimeout(60000)
connect(0)

def writeWebSocket(ws: WebSocket) {
  if (!ws.writeQueueFull()) {
    ws.writeBinaryFrame(Buffer(message))
    vertx.runOnContext({
      writeWebSocket(ws)
    })
  } else {
    // Flow control
    ws.drainHandler({
      writeWebSocket(ws)
    })
  }
}