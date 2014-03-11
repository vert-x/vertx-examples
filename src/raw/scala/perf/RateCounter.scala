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

class RateCounter extends Verticle with Handler[Message[Int]] { this: (Message[Int] => Unit) =>

  private var last: Long = 0
  private var count: Long = 0
  private var vstart: Long = 0
  private var totCount: Long = 0

  def handle(msg: Message[Int]): Unit = {
    if (last == 0) {
      last = System.currentTimeMillis
      vstart = System.currentTimeMillis
    }
    count += msg.body()
    totCount += msg.body()
  }

  override def start() {
    vertx.eventBus.registerHandler("rate-counter", this)
    vertx.setPeriodic(3000, { id: Long =>
      if (last != 0) {
        val now = System.currentTimeMillis
        val rate = 1000 * count / (now - last)
        val avRate = 1000 * totCount / (now - vstart)
        count = 0
        container.logger.info((now - vstart) + " Rate: count/sec: " + rate + " Average rate: " + avRate)
        last = now
      }
    })
  }
}