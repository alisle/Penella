package org.penella.shard

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.Test
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import org.penella.MailBoxes
import org.penella.codecs.IndexResultSetCodec
import org.penella.codecs.StatusMessageCodec
import org.penella.index.IndexType
import org.penella.index.IndexVertical
import org.penella.index.IndexVerticalTest

import org.penella.index.bstree.BSTreeIndexFactory
import org.penella.messages.*
import org.penella.shards.Shard
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
 * Created by alisle on 11/30/16.
 */
@RunWith(VertxUnitRunner::class)
class ShardsTest {
    companion object {
        val log = LoggerFactory.getLogger(IndexVerticalTest::class.java)
    }

    val vertx : Vertx = Vertx.vertx()

    @Before
    fun setUp(context: TestContext) {
        Shard.registerCodecs(vertx)
        IndexVertical.registerCodecs(vertx)
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
    }

    @Test
    fun testVertxAdd() {
        val shard = Shard("TestAdd", BSTreeIndexFactory())
        vertx.deployVerticle(shard)

        var countDown = CountDownLatch(100)
        (0L until 100L).forEach { x -> vertx.eventBus().send(MailBoxes.SHARD_ADD.mailbox + "TestAdd", ShardAdd(HashTriple(x, x, x)), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            countDown.countDown()
        }) }

        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
        Assert.assertEquals(100, shard.size())
        countDown = CountDownLatch(1)

        vertx.eventBus().send(MailBoxes.SHARD_SIZE.mailbox + "TestAdd", ShardSize(), Handler<AsyncResult<Message<ShardSizeResponse>>> { reply ->
            Assert.assertEquals(100, reply.result().body().size)
            countDown.countDown()
        })

        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))

    }


    @Test
    fun testAdd() {
        val shard = Shard("Test", BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, x, x)) }
        Assert.assertEquals(shard.size(), 100)
    }

    @Test
    fun testVertxGetS() {

        val shard = Shard("TestGetS", BSTreeIndexFactory())
        vertx.deployVerticle(shard)

        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, x, x)) }
        Assert.assertEquals(100, shard.size())

        var countDown = CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.SHARD_GET.mailbox + "TestGetS", ShardGet(IndexType.SPO, HashTriple(1000L, 0L, 0L)), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            val results = reply.result().body()
            Assert.assertEquals(100, results.triples.size)

            (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, it, it))) }
            countDown.countDown()

        })
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
    }

    @Test
    fun testGetS() {
        val shard = Shard("Test", BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, x, x)) }
        val results = shard.get(IndexType.SPO, HashTriple(1000L, 0L, 0L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, it, it))) }
    }

    @Test
    fun testVertxGetP() {
        val shard = Shard("TestGetP", BSTreeIndexFactory())
        vertx.deployVerticle(shard)


        var countDown = CountDownLatch(100)
        (0L until 100L).forEach { x -> vertx.eventBus().send(MailBoxes.SHARD_ADD.mailbox + "TestGetP", ShardAdd(HashTriple(x, 1000L, x)), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            countDown.countDown()
        }) }
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))

        countDown = CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.SHARD_GET.mailbox + "TestGetP", ShardGet(IndexType.PO, HashTriple(0L, 1000L, 0L)), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            val results = reply.result().body()
            Assert.assertEquals(results.triples.size, 100)

            (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, 1000L, it))) }
            countDown.countDown()
        })
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
    }


    @Test
    fun testGetP() {
        val shard = Shard("Test", BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, 1000L, x)) }
        val results = shard.get(IndexType.PO, HashTriple(0L, 1000L, 0L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, 1000L, it))) }
    }

    @Test
    fun testVertxGetO() {
        val shard = Shard("TestGetO", BSTreeIndexFactory())
        vertx.deployVerticle(shard)


        var countDown = CountDownLatch(100)
        (0L until 100L).forEach { x -> vertx.eventBus().send(MailBoxes.SHARD_ADD.mailbox + "TestGetO", ShardAdd(HashTriple(x, x, 1000L)), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            countDown.countDown()
        }) }
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))

        countDown = CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.SHARD_GET.mailbox + "TestGetO", ShardGet(IndexType.O, HashTriple(0L, 0L, 1000L)), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            val results = reply.result().body()
            Assert.assertEquals(results.triples.size, 100)

            (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, it, 1000L))) }
            countDown.countDown()
        })

        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
    }

    @Test
    fun testGetO() {
        val shard = Shard("Test", BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, x, 1000L)) }
        val results = shard.get(IndexType.O, HashTriple(0L, 0L, 1000L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, it, 1000L))) }
    }

    @Test
    fun testVertxGetSPO() {
        val shard = Shard("TestGetSPO", BSTreeIndexFactory())
        vertx.deployVerticle(shard)

        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, 2000L, x)) }
        Assert.assertEquals(100, shard.size())

        var countDown = CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.SHARD_GET.mailbox + "TestGetSPO", ShardGet(IndexType.SPO, HashTriple(1000L, 2000L, 0L)), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            val results = reply.result().body()
            //Assert.assertEquals(results.triples.size, 100)
            log.debug("Received ${results.triples.size} results")
            log.debug("Direct query: " + shard.get(IndexType.SPO, HashTriple(1000L, 2000L, 0L)).triples.size)

            (0L until 100L).forEach {
                log.debug("Testing 1000L-2000L-$it")
                Assert.assertTrue(results.triples.contains(HashTriple(1000L, 2000L, it)))
            }
            countDown.countDown()
        })

        Assert.assertTrue(countDown.await(10, TimeUnit.SECONDS))
    }

    @Test
    fun testGetSPO() {
        val shard = Shard("Test", BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, 2000L, x)) }
        val results = shard.get(IndexType.SPO, HashTriple(1000L, 2000L, 0L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, 2000L, it))) }
    }

    @Test
    fun testVertxGetPO() {
        val shard = Shard("TestGetPO", BSTreeIndexFactory())
        vertx.deployVerticle(shard)


        var countDown = CountDownLatch(100)
        (0L until 100L).forEach { x -> vertx.eventBus().send(MailBoxes.SHARD_ADD.mailbox + "TestGetPO", ShardAdd(HashTriple(x, 2000L, 1000L)), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            countDown.countDown()
        }) }
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))

        countDown = CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.SHARD_GET.mailbox + "TestGetPO", ShardGet(IndexType.PO, HashTriple(0L, 2000L, 1000L)), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            val results = reply.result().body()
            Assert.assertEquals(results.triples.size, 100)

            (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, 2000L, 1000L))) }
            countDown.countDown()

        })
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
    }


    @Test
    fun testGetPO() {
        val shard = Shard("Test", BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, 2000L, 1000L)) }
        val results = shard.get(IndexType.PO, HashTriple(0L, 2000L, 1000L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, 2000L, 1000L))) }
    }

    @Test
    fun testVertxGetSO() {
        val shard = Shard("TestGetSO", BSTreeIndexFactory())
        vertx.deployVerticle(shard)


        var countDown = CountDownLatch(100)
        (0L until 100L).forEach { x -> vertx.eventBus().send(MailBoxes.SHARD_ADD.mailbox + "TestGetSO", ShardAdd(HashTriple(1000L, x, 2000L)), Handler<AsyncResult<Message<StatusMessage>>> { reply ->
            countDown.countDown()
        }) }
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))

        countDown = CountDownLatch(1)
        vertx.eventBus().send(MailBoxes.SHARD_GET.mailbox + "TestGetSO", ShardGet(IndexType.SO, HashTriple(1000L, 0L, 2000L)), Handler<AsyncResult<Message<IndexResultSet>>> { reply ->
            val results = reply.result().body()
            Assert.assertEquals(results.triples.size, 100)

            (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, it, 2000L))) }
            countDown.countDown()

        })
        Assert.assertTrue(countDown.await(4, TimeUnit.SECONDS))
    }


    @Test
    fun testGetSO() {
        val shard = Shard("Test", BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, x, 2000L)) }
        val results = shard.get(IndexType.SO, HashTriple(1000L, 0L, 2000L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, it, 2000L))) }
    }
}