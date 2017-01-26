package org.penella.shards

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import org.penella.MailBoxes
import org.penella.codecs.ShardAddCodec
import org.penella.codecs.ShardGetCodec
import org.penella.codecs.ShardSizeCodec
import org.penella.codecs.ShardSizeResponseCodec
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
 * Created by alisle on 12/4/16.
 */
class ShardVertical(val name : String, val shard: IShard) : AbstractVerticle() {
    private fun registerCodecs() {
        vertx.eventBus().registerDefaultCodec(ShardGet::class.java, ShardGetCodec())
        vertx.eventBus().registerDefaultCodec(ShardSize::class.java, ShardSizeCodec())
        vertx.eventBus().registerDefaultCodec(ShardSizeResponse::class.java, ShardSizeResponseCodec())
        vertx.eventBus().registerDefaultCodec(ShardAdd::class.java, ShardAddCodec())
    }

    private val sizeHandler = Handler<Message<ShardSize>> { msg ->
        msg.reply(ShardSizeResponse(shard.size()))
    }

    private val getHandler = Handler<Message<ShardGet>> { msg ->
        val get = msg.body()
        val type = get.indexType
        val triple = get.triple

        val results = shard.get(type, triple)
        msg.reply(results)
    }

    private val addHandler = Handler<Message<ShardAdd>> { msg ->
        val triple = msg.body().triple
        shard.add(triple)

        msg.reply(StatusMessage(Status.SUCESSFUL, "OK"))
    }

    override fun start(startFuture: Future<Void>?) {
        registerCodecs()
        vertx.eventBus().consumer<ShardGet>(MailBoxes.SHARD_GET.mailbox + name).handler(getHandler)
        vertx.eventBus().consumer<ShardSize>(MailBoxes.SHARD_SIZE.mailbox + name).handler(sizeHandler)
        vertx.eventBus().consumer<ShardAdd>(MailBoxes.SHARD_ADD.mailbox + name).handler(addHandler)

        startFuture?.complete()
    }
}