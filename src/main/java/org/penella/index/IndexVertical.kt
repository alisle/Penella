package org.penella.index

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import org.penella.MailBoxes
import org.penella.codecs.IndexAddCodec
import org.penella.codecs.IndexGetFirstLayerCodec
import org.penella.codecs.IndexGetSecondLayerCodec
import org.penella.codecs.IndexResultSetCodec
import org.penella.messages.*


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
class IndexVertical(val name: String, val index: IIndex) : AbstractVerticle() {
    companion object {
         fun registerCodecs(vertx: Vertx) {
            vertx.eventBus().registerDefaultCodec(IndexAdd::class.java, IndexAddCodec())
            vertx.eventBus().registerDefaultCodec(IndexGetFirstLayer::class.java, IndexGetFirstLayerCodec())
            vertx.eventBus().registerDefaultCodec(IndexGetSecondLayer::class.java, IndexGetSecondLayerCodec())
            vertx.eventBus().registerDefaultCodec(IndexResultSet::class.java, IndexResultSetCodec())
        }
    }

    private val addTripleHandler = Handler<Message<IndexAdd>> { msg ->
        val triple = msg.body().triple
        index.add(triple)

        msg.reply(StatusMessage(Status.SUCESSFUL, "OK"))
    }

    private val getFirstLayerHandler = Handler<Message<IndexGetFirstLayer>> { msg ->
        val first = msg.body().first
        val type = msg.body().type

        val results = index.get(type, first)
        msg.reply(results)
    }

    private val getSecondLayerHandler = Handler<Message<IndexGetSecondLayer>> { msg ->
        val firstType = msg.body().firstType
        val firstValue = msg.body().firstValue

        val secondType = msg.body().secondType
        val secondValue = msg.body().secondValue

        val results = index.get(firstType, secondType, firstValue, secondValue)
        msg.reply(results)
    }


    override fun start(startFuture: Future<Void>?) {
        vertx.eventBus().consumer<IndexAdd>(MailBoxes.INDEX_ADD_TRIPLE.mailbox + name).handler(addTripleHandler)
        vertx.eventBus().consumer<IndexGetFirstLayer>(MailBoxes.INDEX_GET_FIRST.mailbox + name).handler(getFirstLayerHandler)
        vertx.eventBus().consumer<IndexGetSecondLayer>(MailBoxes.INDEX_GET_SECOND.mailbox + name).handler(getSecondLayerHandler)

        startFuture?.complete()
    }

}