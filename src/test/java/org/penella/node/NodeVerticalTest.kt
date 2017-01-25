package org.penella.node

import io.vertx.core.AsyncResult
import io.vertx.core.DeploymentOptions
import io.vertx.core.Handler
import io.vertx.core.Vertx
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
import org.penella.store.StoreVertical
import org.slf4j.LoggerFactory
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

@RunWith(VertxUnitRunner::class)
class NodeVerticalTest {
    companion object {
        val log = LoggerFactory.getLogger(NodeVerticalTest::class.java)
    }

    val vertx : Vertx = Vertx.vertx()

    @Before
    fun setUp(context: TestContext) {
        val config = JsonObject()
        config.apply {
            put(NodeVertical.INDEX_FACTORY_TYPE, "BSTreeTreeIndexFactory")
        }

        val deploymentOptions = DeploymentOptions().setConfig(config)
        vertx.deployVerticle(NodeVertical::class.java.name, deploymentOptions, context.asyncAssertSuccess())
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
    }

    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }

    @Test
    fun testAddListDBS() {
        val countDown = CountDownLatch(3)

        vertx.eventBus().send(MailBoxes.NODE_CREATE_DB.mailbox, CreateDB("Test 1", 10), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            log.debug("Created First DB")
            Assert.assertFalse(reply.failed())
            Assert.assertEquals(reply.result().body().status, Status.SUCESSFUL)
            countDown.countDown()
        })

        vertx.eventBus().send(MailBoxes.NODE_CREATE_DB.mailbox, CreateDB("Test 2", 10), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            log.debug("Created Second DB")
            Assert.assertFalse(reply.failed())
            Assert.assertEquals(reply.result().body().status, Status.SUCESSFUL)
            countDown.countDown()
        })


        vertx.eventBus().send(MailBoxes.NODE_LIST_DBS.mailbox, ListDB(), Handler<AsyncResult<Message<ListDBResponse>>> { reply ->
            log.debug("Listed DBs")

            Assert.assertFalse(reply.failed())
            val response = reply.result().body()
            Assert.assertEquals(response.names.size, 2)
            Assert.assertTrue(response.names.contains("Test 1"))
            Assert.assertTrue(response.names.contains("Test 2"))
            countDown.countDown()
        })

        Assert.assertTrue("Timed out waiting for Response", countDown.await(10, TimeUnit.SECONDS))
    }

}