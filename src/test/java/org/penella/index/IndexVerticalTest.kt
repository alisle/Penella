package org.penella.index

import io.vertx.core.AsyncResult
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
import org.penella.index.bstree.SubjectPropertyObjectBSTreeIndex
import org.penella.messages.*
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.TripleType
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
 * Created by alisle on 1/25/17.
 */

@RunWith(VertxUnitRunner::class)
class IndexVerticalTest {
    companion object {
        val log = LoggerFactory.getLogger(IndexVerticalTest::class.java)
    }

    val vertx : Vertx = Vertx.vertx()

    @Before
    fun setUp(context: TestContext) {
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
        vertx.deployVerticle(IndexVertical("test", SubjectPropertyObjectBSTreeIndex()))
    }

    @Test
    fun testAddGet() {
        var countDown =  CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.INDEX_ADD_TRIPLE.mailbox + "test", IndexAdd(HashTriple(10L, 100L, 1000L)), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            log.debug("Added Triple")
            countDown.countDown()
        })

        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))

        countDown =  CountDownLatch(2)

        vertx.eventBus().send(MailBoxes.INDEX_GET_FIRST.mailbox + "test", IndexGetFirstLayer(TripleType.SUBJECT, 10L), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            log.debug("Got First Results")
            Assert.assertEquals(reply.result().body().triples.size, 1)
            countDown.countDown()
        })

        vertx.eventBus().send(MailBoxes.INDEX_GET_SECOND.mailbox + "test", IndexGetSecondLayer(TripleType.SUBJECT, 10L, TripleType.PROPERTY, 100L), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            log.debug("Got Second Results")
            val results = reply.result().body()
            Assert.assertEquals(results.triples.size, 1)
            countDown.countDown()
        })

        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
    }


    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }
}