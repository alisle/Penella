package org.penella.database

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import org.penella.codecs.DatabaseAddTripleCodec
import org.penella.codecs.DatabaseBulkAddTriplesCodec
import org.penella.codecs.DatabaseQueryCodec
import org.penella.codecs.DatabaseQueryResultCodec
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
 * Created by alisle on 1/26/17.
 */
class DatabaseVertical(val database : IDatabase) : AbstractVerticle() {
    private fun registerCodecs() {
        vertx.eventBus().registerDefaultCodec(DatabaseAddTriple::class.java, DatabaseAddTripleCodec())
        vertx.eventBus().registerDefaultCodec(DatabaseBulkAddTriples::class.java, DatabaseBulkAddTriplesCodec())
        vertx.eventBus().registerDefaultCodec(DatabaseQuery::class.java, DatabaseQueryCodec())
        vertx.eventBus().registerDefaultCodec(DatabaseQueryResult::class.java, DatabaseQueryResultCodec())
    }

    private val addHandler = Handler<Message<DatabaseAddTriple>> { msg ->
        database.add(msg.body().triple)
        msg.reply(StatusMessage(Status.SUCESSFUL, "OK"))
    }


    override fun start(startFuture: Future<Void>?) {
        registerCodecs()

        startFuture?.complete()
    }
}