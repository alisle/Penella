package org.penella.rest

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import org.penella.MailBoxes
import org.penella.messages.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by alisle on 12/12/16.
 */

class RESTVertical : AbstractVerticle() {


    override fun start(startFuture: Future<Void>?) {
        val router = createRouter()

        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(config().getInteger("http.port", 8080)) { result ->
                    if (result.succeeded()) {
                        startFuture?.complete()
                    } else {
                        startFuture?.fail(result.cause())
                    }
                }
    }

    private fun createRouter() = Router.router(vertx).apply {
        route().handler(BodyHandler.create())
        post("/_add").consumes("*/json").produces("application/json").blockingHandler(handlerAddDatabase)
        get("/_list").produces("application/json").blockingHandler(handlerListDatabase)
    }

    private fun statusMessageHandler(latch: CountDownLatch, future: Future<StatusMessage>) = Handler<AsyncResult<Message<StatusMessage>>> { reply ->
        if (reply.failed()) {
            future.fail(reply.cause())
        } else {
            val statusMsg: StatusMessage = reply.result().body()
            future.complete(statusMsg)
        }

        latch.countDown()
    }

    private fun statusMessageResponseHandler(latch: CountDownLatch, future: Future<StatusMessage>, response: HttpServerResponse) {
        if(latch.await(400, TimeUnit.SECONDS)) {
            val result = future.result()!!
            response.endWithJson(result)
            when(result.status) {
                Status.FAILED -> response.statusCode = 500
                Status.SUCESSFUL -> response.statusCode = 200
                Status.IN_PROGRESS -> response.statusCode = 200
            }
        } else {
            response.statusCode = 503
            response.endWithJson("Timeout waiting for command to finish")
        }

    }

    val handlerAddDatabase = Handler<RoutingContext> { req ->
        val createDB = Json.decodeValue(req.bodyAsString, CreateDB::class.java)
        val latch : CountDownLatch = CountDownLatch(1)
        val future : Future<StatusMessage> = Future.future()
        vertx.eventBus().send(MailBoxes.NODE_CREATE_DB.mailbox, createDB, statusMessageHandler(latch, future))
        statusMessageResponseHandler(latch, future, req.response())
    }

    val handlerListDatabase = Handler<RoutingContext> { req ->
        val latch : CountDownLatch = CountDownLatch(1)
        val future : Future<ListDBResponse> = Future.future()
        val response = req.response()

        vertx.eventBus().send(MailBoxes.NODE_LIST_DBS.mailbox, ListDB(), Handler<AsyncResult<Message<ListDBResponse>>>  { reply ->
            if (reply.failed()) {
                future.fail(reply.cause())
            } else {
                val list: ListDBResponse= reply.result().body()
                future.complete(list)
            }

            latch.countDown()
        })

        if(latch.await(400, TimeUnit.SECONDS)) {
            val result = future.result()!!
            response.endWithJson(result)
        } else {
            response.statusCode = 503
            response.endWithJson("Timeout waiting for command to finish")
        }
    }

    fun HttpServerResponse.endWithJson(obj: Any) {
        this.putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encodePrettily(obj))
    }

}