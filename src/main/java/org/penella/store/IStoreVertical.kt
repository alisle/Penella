package org.penella.store

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import org.omg.PortableInterceptor.SUCCESSFUL
import org.penella.MailBoxes
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
class IStoreVertical : AbstractVerticle() {
    companion object {
        private val STORE_TYPE = "type"
        private val SEED = "seed"
        private val MAX_STRING = "max_string"
    }

    val storeType = config().getString(STORE_TYPE)
    val storeSeed = config().getLong(SEED)
    val storeMaxString = config().getInteger(MAX_STRING)

    val store =  when(storeType) {
        "BSTreeStore" -> BSTreeStore(storeSeed)
        "BTreeCompressedStore" -> BTreeCompressedStore(storeSeed, storeMaxString)
    }

    override fun start(startFuture: Future<Void>?) {
        vertx.eventBus().consumer<StoreAddString>(MailBoxes.STORE_ADD_STRING.mailbox).handler(storeAddStringHandler())
        vertx.eventBus().consumer<StoreAddTriple>(MailBoxes.STORE_ADD_TRIPLE.mailbox).handler(storeAddTripleHandler())
        vertx.eventBus().consumer<StoreGetString>(MailBoxes.STORE_GET_STRING.mailbox).handler(getStringHandler())

        startFuture.complete()
    }

    fun storeAddTripleHandler = Handler<Message<StoreAddTriple>> { msg ->
        store.add(msg.body().value)
        msg.reply(StatusMessage(Status.SUCESSFUL, "Successfully Added"))
    }

    fun storeAddStringHandler = Handler<Message<StoreAddString>> { msg ->
        val hash = store.add(msg.body().value)
        msg.reply(StoreAddStringResponse(hash))
    }

    fun getStringHandler = Handler<Message<StoreGetString>> { msg ->
        val string = store.get(msg.body().value)
        msg.reply(StoreGetStringResponse(string))
    }

    override fun stop(stopFuture: Future<Void>?) {
        super.stop(stopFuture)
    }
}
