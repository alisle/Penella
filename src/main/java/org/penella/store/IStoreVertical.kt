package org.penella.store

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import org.omg.PortableInterceptor.SUCCESSFUL
import org.penella.MailBoxes
import org.penella.codecs.*
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
 * Created by alisle on 1/14/17.
 */

class InvalidStoreSelected : Exception("Invalid Store Selected")
class IStoreVertical : AbstractVerticle() {
    companion object {
        val STORE_TYPE = "type"
        val SEED = "seed"
        val MAX_STRING = "max_string"
    }

    val storeType : String by lazy {
        config().getString(STORE_TYPE)
    }
    val storeSeed : Long by lazy {
        config().getLong(SEED)
    }
    val storeMaxString : Int by lazy {
        config().getInteger(MAX_STRING)
    }
    val store : IStore by lazy {
        when(storeType) {
            "BSTreeStore" -> BSTreeStore(storeSeed)
            "BTreeCompressedStore" -> BTreeCompressedStore(storeSeed, storeMaxString)
            else -> throw InvalidStoreSelected()
        }
    }

    val storeAddTripleHandler = Handler<Message<StoreAddTriple>> { msg ->
        store.add(msg.body().value)
        msg.reply(StatusMessage(Status.SUCESSFUL, "Successfully Added"))
    }

    val storeAddStringHandler = Handler<Message<StoreAddString>> { msg ->
        val hash = store.add(msg.body().value)
        msg.reply(StoreAddStringResponse(hash))
    }

    val getStringHandler = Handler<Message<StoreGetString>> { msg ->
        val string = store.get(msg.body().value)
        msg.reply(StoreGetStringResponse(string))
    }

    val getHashtripleHandler = Handler<Message<StoreGetHashTriple>> { msg ->
        val triple = store.get(msg.body().value)
        msg.reply(StoreGetHashTripleResponse(triple))
    }

    val generateHashHandler = Handler<Message<StoreGenerateHash>> { msg ->
        val hash = store.generateHash(msg.body().value)
        msg.reply(StoreGenerateHashResponse(hash))
    }

    fun registerCodecs() {
        vertx.eventBus().registerDefaultCodec(StoreAddString::class.java, StoreAddStringCodec())
        vertx.eventBus().registerDefaultCodec(StoreAddStringResponse::class.java, StoreAddStringResponseCodec())
        vertx.eventBus().registerDefaultCodec(StoreAddTriple::class.java, StoreAddTripleCodec())
        vertx.eventBus().registerDefaultCodec(StoreGenerateHash::class.java, StoreGenerateHashCodec())
        vertx.eventBus().registerDefaultCodec(StoreGenerateHashResponse::class.java, StoreGenerateHashResponseCodec())
        vertx.eventBus().registerDefaultCodec(StoreGetHashTriple::class.java, StoreGetHashTripleCodec())
        vertx.eventBus().registerDefaultCodec(StoreGetHashTripleResponse::class.java, StoreGetHashTripleResponseCodec())
        vertx.eventBus().registerDefaultCodec(StoreGetString::class.java, StoreGetStringCodec())
        vertx.eventBus().registerDefaultCodec(StoreGetStringResponse::class.java, StoreGetStringResponseCodec())
    }
    override fun start(startFuture: Future<Void>?) {

        registerCodecs()

        vertx.eventBus().consumer<StoreAddString>(MailBoxes.STORE_ADD_STRING.mailbox).handler(storeAddStringHandler)
        vertx.eventBus().consumer<StoreAddTriple>(MailBoxes.STORE_ADD_TRIPLE.mailbox).handler(storeAddTripleHandler)
        vertx.eventBus().consumer<StoreGetString>(MailBoxes.STORE_GET_STRING.mailbox).handler(getStringHandler)
        vertx.eventBus().consumer<StoreGetHashTriple>(MailBoxes.STORE_GET_HASHTRIPLE.mailbox).handler(getHashtripleHandler)
        vertx.eventBus().consumer<StoreGenerateHash>(MailBoxes.STORE_GENERATE_HASH.mailbox).handler(generateHashHandler)

        startFuture?.complete()
    }

    override fun stop(stopFuture: Future<Void>?) {
        super.stop(stopFuture)
    }
}
