package org.penella.store

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import org.penella.MailBoxes
import org.penella.messages.*
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.Triple
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
 * Created by alisle on 1/24/17.
 */

class StoreTimedOutException : Exception("Timed out while querying store!")
class VerticalStoreWrapper(val vertx: Vertx) : IStore {

    override fun add(value: String): Long {
        val latch = CountDownLatch(1)
        val future = Future.future<StoreAddStringResponse>()

        vertx.eventBus().send(MailBoxes.STORE_ADD_STRING.mailbox, StoreAddString(value), Handler<AsyncResult<Message<StoreAddStringResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            return future.result().hash
        } else {
            throw  StoreTimedOutException()
        }

    }

    override fun add(triple: Triple) {
        vertx.eventBus().send(MailBoxes.STORE_ADD_TRIPLE.mailbox, StoreAddTriple(triple))
    }

    override fun get(value: Long): String? {
        val latch = CountDownLatch(1)
        val future = Future.future<StoreGetStringResponse>()

        vertx.eventBus().send(MailBoxes.STORE_GET_STRING.mailbox, StoreGetString(value), Handler<AsyncResult<Message<StoreGetStringResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            return future.result().string
        } else {
            throw  StoreTimedOutException()
        }

    }

    override fun get(hashTriple: HashTriple): Triple? {
        val latch = CountDownLatch(1)
        val future = Future.future<StoreGetHashTripleResponse>()

        vertx.eventBus().send(MailBoxes.STORE_GET_HASHTRIPLE.mailbox, StoreGetHashTriple(hashTriple), Handler<AsyncResult<Message<StoreGetHashTripleResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            return future.result().value
        } else {
            throw  StoreTimedOutException()
        }

    }

    override fun generateHash(value: String): Long {
        val latch = CountDownLatch(1)
        val future = Future.future<StoreGenerateHashResponse>()

        vertx.eventBus().send(MailBoxes.STORE_GENERATE_HASH.mailbox, StoreGenerateHash(value), Handler<AsyncResult<Message<StoreGenerateHashResponse>>> { reply ->
            if(reply.failed()) {
                future.fail(reply.cause())
            } else {
                val response = reply.result().body()
                future.complete(response)
            }
            latch.countDown()
        })

        if(latch.await(4, TimeUnit.SECONDS)) {
            return future.result().hash ?: -1L
        } else {
            throw  StoreTimedOutException()
        }

    }
}