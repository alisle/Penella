package org.penella.rest

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import org.penella.MailBoxes
import org.penella.messages.*
import org.penella.structures.triples.HashTriple
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by alisle on 1/15/17.
 */
class StoreRouter(val vertx: Vertx) {
    val router by lazy {  createRouter() }

    private fun createRouter() = Router.router(vertx).apply {
        route().handler(BodyHandler.create())
        post("/string").consumes("*/json").produces("application/json").blockingHandler(handlerAddString)
        post("/triple").consumes("*/json").produces("application/json").blockingHandler(handlerAddTriple)
        get("/string/:hash").produces("application/json").blockingHandler(handlerGetString)
        get("/triple/:subject/:property/:object").produces("application/json").blockingHandler(handlerGetTriple)
        get("/hash/:hash").produces("application/json").blockingHandler(handlerGetHash)
    }

    val handlerAddString = Handler<RoutingContext> { req ->
        // TODO: We have this pattern everywhere we need to capsulate it
        val addString = Json.decodeValue(req.bodyAsString, StoreAddString::class.java)
        val latch = CountDownLatch(1)
        val future = Future.future<StoreAddStringResponse>()

        vertx.eventBus().send(MailBoxes.STORE_ADD_STRING.mailbox, addString, Handler<AsyncResult<Message<StoreAddStringResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            req.response().statusCode = if(future.failed()) 500 else 200
            req.response().endWithJson(future.result())
        } else {
            req.response().statusCode = 500
            req.response().endWithJson("Timeout waiting for command to finish")
        }
    }

    val handlerAddTriple = Handler<RoutingContext> { req ->
        val addTriple = Json.decodeValue(req.bodyAsString, StoreAddTriple::class.java)
        val latch = CountDownLatch(1)
        val future = Future.future<StatusMessage>()

        vertx.eventBus().send(MailBoxes.STORE_ADD_TRIPLE.mailbox, addTriple, Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            req.response().statusCode = if(future.failed()) 500 else 200
            req.response().endWithJson(future.result())
        } else {
            req.response().statusCode = 500
            req.response().endWithJson("Timeout waiting for command to finish")
        }
    }

    val handlerGetString = Handler<RoutingContext> { req ->
        val getString = StoreGetString(req.request().getParam("hash").toLong())
        val latch = CountDownLatch(1)
        val future = Future.future<StoreGetStringResponse>()

        vertx.eventBus().send(MailBoxes.STORE_GET_STRING.mailbox, getString, Handler<AsyncResult<Message<StoreGetStringResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            req.response().statusCode = if(future.failed()) 500 else 200
            req.response().endWithJson(future.result())
        } else {
            req.response().statusCode = 500
            req.response().endWithJson("Timeout waiting for command to finish")
        }
    }
    val handlerGetTriple = Handler<RoutingContext> { req ->
        val subject = req.request().getParam("subject").toLong()
        val property = req.request().getParam("property").toLong()
        val obj = req.request().getParam("object").toLong()
        val triple = HashTriple(subject, property, obj)
        val getTriple = StoreGetHashTriple(triple)

        val latch = CountDownLatch(1)
        val future = Future.future<StoreGetHashTripleResponse>()

        vertx.eventBus().send(MailBoxes.STORE_GET_HASHTRIPLE.mailbox, getTriple, Handler<AsyncResult<Message<StoreGetHashTripleResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            req.response().statusCode = if(future.failed()) 500 else 200
            req.response().endWithJson(future.result())
        } else {
            req.response().statusCode = 500
            req.response().endWithJson("Timeout waiting for command to finish")
        }
    }
    val handlerGetHash = Handler<RoutingContext> { req ->
        val hash = req.request().getParam("hash")
        val generateHash = StoreGenerateHash(hash)

        val latch = CountDownLatch(1)
        val future = Future.future<StoreGenerateHashResponse>()

        vertx.eventBus().send(MailBoxes.STORE_GENERATE_HASH.mailbox, generateHash, Handler<AsyncResult<Message<StoreGenerateHashResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            req.response().statusCode = if(future.failed()) 500 else 200
            req.response().endWithJson(future.result())
        } else {
            req.response().statusCode = 500
            req.response().endWithJson("Timeout waiting for command to finish")
        }
    }

    // TODO: Need to extract to a new trait or something
    fun HttpServerResponse.endWithJson(obj: Any) {
        this.putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encodePrettily(obj))
    }
}