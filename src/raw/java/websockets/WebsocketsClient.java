package websockets;

/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.http.WebSocket;
import org.vertx.java.platform.Verticle;

public class WebsocketsClient extends Verticle {

  public void start() {

    // Setting host as localhost is not strictly necessary as it's the default
    vertx.createHttpClient().setHost("localhost").setPort(8080).connectWebsocket("/myapp", new Handler<WebSocket>() {
      @Override
      public void handle(WebSocket websocket) {
        websocket.dataHandler(new Handler<Buffer>() {
          @Override
          public void handle(Buffer data) {
            System.out.println("Received " + data);
          }
        });
        //Send some data
        websocket.writeTextFrame("hello world");
      }
    });

  }
}

