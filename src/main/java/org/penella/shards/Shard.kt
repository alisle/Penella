package org.penella.shards

import io.vertx.core.*
import io.vertx.core.eventbus.Message
import org.penella.MailBoxes
import org.penella.codecs.ShardAddCodec
import org.penella.codecs.ShardGetCodec
import org.penella.codecs.ShardSizeCodec
import org.penella.codecs.ShardSizeResponseCodec
import org.penella.index.IIndexFactory
import org.penella.index.IndexType
import org.penella.index.IndexVertical
import org.penella.index.bstree.InvalidIndexRequest
import org.penella.messages.*
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.TripleType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicLong

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
 * Created by alisle on 11/1/16.
 */
class Shard constructor(private val name : String, private val indexFactory: IIndexFactory) : IShard, AbstractVerticle() {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(Shard::class.java)

        fun registerCodecs(vertx: Vertx) {
            try {
                vertx.eventBus().registerDefaultCodec(ShardGet::class.java, ShardGetCodec())
                vertx.eventBus().registerDefaultCodec(ShardSize::class.java, ShardSizeCodec())
                vertx.eventBus().registerDefaultCodec(ShardSizeResponse::class.java, ShardSizeResponseCodec())
                vertx.eventBus().registerDefaultCodec(ShardAdd::class.java, ShardAddCodec())
            } catch (exception : java.lang.IllegalStateException) {
                log.info("Unable to register codecs, most likely already registered")
            }
        }
    }

    private val indexes = Array(IndexType.values().size, { i -> indexFactory.createIndex(IndexType.values()[i]) })


    private val sizeHandler = Handler<Message<ShardSize>> { msg ->
        msg.reply(ShardSizeResponse(size()))
    }

    private val getHandler = Handler<Message<ShardGet>> { msg ->
        val get = msg.body()
        val type = get.indexType
        val triple = get.triple

        if(log.isDebugEnabled) log.debug("VERTX Grabbing triple: $triple")

        when(type) {
            IndexType.SPO -> {
                if(triple.hashProperty != 0L) {
                    vertx.eventBus().send(MailBoxes.INDEX_GET_SECOND.mailbox + "$name-${type.ID}",
                            IndexGetSecondLayer(TripleType.SUBJECT, triple.hashSubject, TripleType.PROPERTY, triple.hashProperty),
                            Handler<AsyncResult<Message<IndexResultSet>>> { response ->
                        msg.reply(response.result().body())
                    })
                } else {
                    vertx.eventBus().send(MailBoxes.INDEX_GET_FIRST.mailbox + "$name-${type.ID}",
                            IndexGetFirstLayer(TripleType.SUBJECT, triple.hashSubject),
                            Handler<AsyncResult<Message<IndexResultSet>>> { response ->
                                msg.reply(response.result().body())
                            })
                }
            }
            IndexType.O -> {
                if(triple.hashProperty == 0L && triple.hashSubject == 0L) {
                    vertx.eventBus().send(MailBoxes.INDEX_GET_FIRST.mailbox + "$name-${type.ID}",
                            IndexGetFirstLayer(TripleType.OBJECT, triple.hashObj),
                            Handler<AsyncResult<Message<IndexResultSet>>> { response ->
                                msg.reply(response.result().body())
                            })
                } else {
                    throw InvalidIndexRequest();
                }
            }
            IndexType.PO -> {
                if(triple.hashObj != 0L) {
                    vertx.eventBus().send(MailBoxes.INDEX_GET_SECOND.mailbox + "$name-${type.ID}",
                            IndexGetSecondLayer(TripleType.PROPERTY, triple.hashProperty, TripleType.OBJECT, triple.hashObj),
                            Handler<AsyncResult<Message<IndexResultSet>>> { response ->
                                msg.reply(response.result().body())
                            })

                } else {
                    vertx.eventBus().send(MailBoxes.INDEX_GET_FIRST.mailbox + "$name-${type.ID}",
                            IndexGetFirstLayer(TripleType.PROPERTY, triple.hashProperty),
                            Handler<AsyncResult<Message<IndexResultSet>>> { response ->
                                msg.reply(response.result().body())
                            })
                }
            }
            IndexType.SO -> {
                if(triple.hashObj != 0L) {
                    vertx.eventBus().send(MailBoxes.INDEX_GET_SECOND.mailbox + "$name-${type.ID}",
                            IndexGetSecondLayer(TripleType.SUBJECT, triple.hashSubject, TripleType.OBJECT, triple.hashObj),
                            Handler<AsyncResult<Message<IndexResultSet>>> { response ->
                                msg.reply(response.result().body())
                            })

                } else {
                    vertx.eventBus().send(MailBoxes.INDEX_GET_FIRST.mailbox + "$name-${type.ID}",
                            IndexGetFirstLayer(TripleType.SUBJECT, triple.hashSubject),
                            Handler<AsyncResult<Message<IndexResultSet>>> { response ->
                                msg.reply(response.result().body())
                            })
                }
            }
        }
    }

    private val addHandler = Handler<Message<ShardAdd>> { msg ->
        val triple = msg.body().triple

        if(log.isDebugEnabled)  log.debug("Inserting triple: $triple")

        val futures = Array<Future<Message<StatusMessage>>>(IndexType.values().size, { x -> Future.future<Message<StatusMessage>>() })
        IndexType.values().forEach { type -> vertx.eventBus().send(MailBoxes.INDEX_ADD_TRIPLE.mailbox + "$name-${type.ID}", IndexAdd(triple), futures[type.ID].completer()) }

        val composite = CompositeFuture.all(futures.toList())
        composite.setHandler { result ->
            msg.reply(StatusMessage(Status.SUCESSFUL, "OK"))
            counter.incrementAndGet()
        }

    }

    override fun start(startFuture: Future<Void>?) {
        vertx.eventBus().consumer<ShardGet>(MailBoxes.SHARD_GET.mailbox + name).handler(getHandler)
        vertx.eventBus().consumer<ShardSize>(MailBoxes.SHARD_SIZE.mailbox + name).handler(sizeHandler)
        vertx.eventBus().consumer<ShardAdd>(MailBoxes.SHARD_ADD.mailbox + name).handler(addHandler)

        IndexType.values().forEach { type -> vertx.deployVerticle(IndexVertical("$name-${type.ID}", indexes[type.ID])) }
        startFuture?.complete()
    }

    private val counter = AtomicLong(0L)

    override fun size() = counter.get()

    override fun get(indexType: IndexType, triple: HashTriple): IndexResultSet {
        if(log.isDebugEnabled) log.debug("Grabbing triple: $triple")

        return when(indexType) {
            IndexType.SPO -> {
                if(triple.hashProperty != 0L) {
                    indexes[indexType.ID].get(TripleType.SUBJECT, TripleType.PROPERTY, triple.hashSubject, triple.hashProperty)
                } else {
                    indexes[indexType.ID].get(TripleType.SUBJECT, triple.hashSubject)
                }
            }
            IndexType.O -> {
                if(triple.hashProperty == 0L && triple.hashSubject == 0L) {
                    indexes[indexType.ID].get(TripleType.OBJECT, triple.hashObj)
                } else {
                    throw InvalidIndexRequest();
                }
            }
            IndexType.PO -> {
                if(triple.hashObj != 0L) {
                    indexes[indexType.ID].get(TripleType.PROPERTY, TripleType.OBJECT, triple.hashProperty, triple.hashObj)
                } else {
                    indexes[indexType.ID].get(TripleType.PROPERTY, triple.hashProperty)
                }
            }
            IndexType.SO -> {
                if(triple.hashObj != 0L) {
                    indexes[indexType.ID].get(TripleType.SUBJECT, TripleType.OBJECT, triple.hashSubject, triple.hashObj)
                } else {
                    indexes[indexType.ID].get(TripleType.SUBJECT, triple.hashSubject)
                }
            }
        }
    }

    override fun add(triple: HashTriple) {
        if(log.isDebugEnabled)  log.debug("Inserting triple: $triple")
        counter.incrementAndGet()
        indexes.forEach { x -> x.add(triple) }

    }
}