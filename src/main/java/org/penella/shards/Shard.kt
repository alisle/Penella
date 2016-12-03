package org.penella.shards

import io.vertx.core.Future
import org.penella.index.IIndexFactory
import org.penella.index.IndexType
import org.penella.query.IQuery
import org.penella.query.IncompleteResultSet
import org.penella.structures.triples.HashTriple
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
class Shard constructor(private val indexFactory: IIndexFactory) : IShard  {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(Shard::class.java)
    }

    private val indexes = Array(IndexType.values().size, { i -> indexFactory.createIndex(IndexType.values()[i]) })
    private val counter = AtomicLong(0L)

    override fun size() = counter.get()

    override fun query(query: IQuery): IncompleteResultSet {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun add(triple: HashTriple) {
        if(log.isDebugEnabled)  log.debug("Inserting triple: $triple")
        counter.incrementAndGet()
        indexes.forEach { x -> x.add(triple) }
    }
}