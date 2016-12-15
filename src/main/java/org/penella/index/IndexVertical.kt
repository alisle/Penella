package org.penella.index

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message

import org.penella.messages.AddTriple
import org.penella.messages.GetDoublerResult
import org.penella.messages.GetSingleResult

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
class IndexVertical(dbName: String, shardName: String, indexName: String, private val index: IIndex) : AbstractVerticle() {
    private val mailBoxStub = "$dbName-$shardName-$indexName-"
    private val mailBoxAddTriple = mailBoxStub + "AddTriple"
    private val mailBoxGetSingleResult = mailBoxStub + "GetSingleResult"
    private val mailboxGetDoubleResult = mailBoxStub + "GetDoubleResult"

    override fun start(startFuture: Future<Void>?) {
        vertx.eventBus().consumer(mailBoxAddTriple, Handler { message: Message<AddTriple> -> index.add(message.body().triple) })
        vertx.eventBus().consumer(mailBoxGetSingleResult, Handler { message: Message<GetSingleResult> -> index.get(message.body().firstType, message.body().firstValue) })
        vertx.eventBus().consumer(mailboxGetDoubleResult, Handler { message: Message<GetDoublerResult> -> index.get(message.body().firstType, message.body().secondType, message.body().firstValue, message.body().secondValue ) })

        startFuture!!.complete()
    }

}