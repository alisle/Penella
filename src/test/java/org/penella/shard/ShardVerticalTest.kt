package org.penella.shard

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.penella.MailBoxes
import org.penella.codecs.IndexResultSetCodec
import org.penella.codecs.StatusMessageCodec
import org.penella.index.IndexType
import org.penella.index.IndexVerticalTest
import org.penella.index.bstree.BSTreeIndexFactory
import org.penella.messages.*
import org.penella.shards.Shard
import org.penella.shards.ShardVertical
import org.penella.structures.triples.HashTriple
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
 * Created by alisle on 1/26/17.
 */

@RunWith(VertxUnitRunner::class)
class ShardVerticalTest {
    companion object {
        val log = LoggerFactory.getLogger(IndexVerticalTest::class.java)
    }

    val vertx : Vertx = Vertx.vertx()

    @Before
    fun setUp(context: TestContext) {
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
        vertx.eventBus().registerDefaultCodec(IndexResultSet::class.java, IndexResultSetCodec())

        vertx.deployVerticle(ShardVertical("test", Shard(BSTreeIndexFactory())))
    }

    @Test
    fun testAddResultSize() {
        var countDown = CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.SHARD_ADD.mailbox + "test", ShardAdd(HashTriple(10L, 100L, 1000L)), Handler<AsyncResult<Message<StatusMessage>>> { msg ->
            Assert.assertEquals(msg.result().body().status, Status.SUCESSFUL)

            log.debug("Added Triple")
            countDown.countDown()
        })

        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
        countDown = CountDownLatch(2)

        vertx.eventBus().send(MailBoxes.SHARD_SIZE.mailbox + "test", ShardSize(), Handler<AsyncResult<Message<ShardSizeResponse>>> { msg ->
            Assert.assertEquals(msg.result().body().size, 1L)
            log.debug("Got Size")
            countDown.countDown()
        })

        vertx.eventBus().send(MailBoxes.SHARD_GET.mailbox + "test", ShardGet(IndexType.SPO, HashTriple(10L, 100L, 1000L)), Handler<AsyncResult<Message<IndexResultSet>>> { msg ->
            Assert.assertEquals(msg.result().body().triples.size, 1)
            log.debug("Got Size")
            countDown.countDown()
        })

        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
    }

}
