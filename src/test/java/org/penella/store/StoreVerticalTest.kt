package org.penella.store

import io.vertx.core.*
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.penella.MailBoxes
import org.penella.codecs.StatusMessageCodec
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
 * Created by alisle on 1/15/17.
 */
@RunWith(VertxUnitRunner::class)
class StoreVerticalTest {
    val vertx : Vertx = Vertx.vertx()

    @Before
    fun setUp(context: TestContext) {
        val config = JsonObject()
        config.apply {
            put(StoreVertical.STORE_TYPE, "BTreeCompressedStore")
            put(StoreVertical.SEED, 665445839L)
            put(StoreVertical.MAX_STRING, 1024)
        }

        val deploymentOptions = DeploymentOptions().setConfig(config)
        vertx.deployVerticle(StoreVertical::class.java.name, deploymentOptions, context.asyncAssertSuccess())
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
    }

    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }


    @Test
    fun testAddString() {
        vertx.eventBus().send(MailBoxes.STORE_ADD_STRING.mailbox, StoreAddString("Hello"), Handler<AsyncResult<Message<StoreAddStringResponse>>> { reply ->
            if(reply.failed()) {
                Assert.assertTrue("Timeout waiting for response!", false)
            } else {
                val response = reply.result().body()
                val hash : Long = response.hash
            }
        })
    }

    @Test
    fun testAddTriple() {
        vertx.eventBus().send(MailBoxes.STORE_ADD_TRIPLE.mailbox, StoreAddTriple(Triple("Subject", "Property", "Object")), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            if(reply.failed()) {
                Assert.assertTrue("Timeout waiting for response!", false)
            } else {
                val response = reply.result().body()
                Assert.assertEquals(response.status, Status.SUCESSFUL)
            }
        })
    }


    @Test
    fun testGetString() {
        vertx.eventBus().send(MailBoxes.STORE_GET_STRING.mailbox, StoreGetString(100L), Handler<AsyncResult<Message<StoreGetStringResponse>>> { reply ->
            if(reply.failed()) {
                Assert.assertTrue("Timeout waiting for response!", false)
            } else {
                val response = reply.result().body()
                Assert.assertEquals(response.string, null)
            }
        })
    }

    @Test
    fun testGetHashTriple() {
        vertx.eventBus().send(MailBoxes.STORE_GET_HASHTRIPLE.mailbox, StoreGetHashTriple(HashTriple(0L, 10L, 100L)), Handler<AsyncResult<Message<StoreGetHashTripleResponse>>> { reply ->
            if(reply.failed()) {
                Assert.assertTrue("Timeout waiting for response!", false)
            } else {
                val response = reply.result().body()
                Assert.assertNull(response.value)
            }
        })
    }

    @Test
    fun testGenerateHash() {
        vertx.eventBus().send(MailBoxes.STORE_GENERATE_HASH.mailbox, StoreGetHashTriple(HashTriple(0L, 10L, 100L)), Handler<AsyncResult<Message<StoreGenerateHashResponse>>> { reply ->
            if(reply.failed()) {
                Assert.assertTrue("Timeout waiting for response!", false)
            } else {
                val response = reply.result().body()
                Assert.assertTrue(response.hash is Long)
            }
        })
    }

}
