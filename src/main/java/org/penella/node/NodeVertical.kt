package org.penella.node

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import org.penella.MailBoxes
import org.penella.codecs.CreateDBCodec
import org.penella.codecs.ListDBCodec
import org.penella.codecs.ListDBResponseCodec
import org.penella.index.IIndexFactory
import org.penella.index.bstree.BSTreeIndexFactory
import org.penella.messages.CreateDB
import org.penella.messages.ListDB
import org.penella.messages.ListDBResponse
import org.penella.store.VerticalStoreWrapper

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
class InvalidIndexFactory : Exception("Invalid Index Factory")
class NodeVertical : AbstractVerticle() {
    companion object {
        val INDEX_FACTORY_TYPE = "index_factory"
    }

    val indexFactoryType : String by lazy {
        config().getString(INDEX_FACTORY_TYPE)
    }

    val indexFactory : IIndexFactory by lazy {
        when(indexFactoryType) {
            "BSTreeTreeIndexFactory" -> BSTreeIndexFactory()
            else -> throw InvalidIndexFactory()
        }
    }

    val node : INode  by lazy {
        Node(VerticalStoreWrapper(vertx), indexFactory)
    }

    private fun registerCodecs() {
        vertx.eventBus().registerDefaultCodec(CreateDB::class.java, CreateDBCodec())
        vertx.eventBus().registerDefaultCodec(ListDB::class.java, ListDBCodec())
        vertx.eventBus().registerDefaultCodec(ListDBResponse::class.java, ListDBResponseCodec())

    }

    override fun start(startFuture: Future<Void>?) {
        registerCodecs()

        vertx.eventBus().consumer<CreateDB>(MailBoxes.NODE_CREATE_DB.mailbox).handler{ msg ->
            msg.reply(node.createDB(msg.body()))
        }

        vertx.eventBus().consumer<ListDB>(MailBoxes.NODE_LIST_DBS.mailbox).handler{ msg ->
            msg.reply(node.listDB(msg.body()))
        }

        startFuture?.complete()
    }

}