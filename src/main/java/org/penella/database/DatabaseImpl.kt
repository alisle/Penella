package org.penella.database

import org.penella.index.IIndexFactory
import org.penella.index.IndexType
import org.penella.query.*
import org.penella.shards.Shard
import org.penella.store.IStore
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.Triple
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

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
 *
 * Created by alisle on 9/29/16.
 */
class DatabaseImpl constructor(val name: String,  private val store: IStore, private val indexFactory: IIndexFactory , numberOfShards: Int) : IDatabase {
    companion object {
        val log : Logger = LoggerFactory.getLogger(DatabaseImpl::class.java)
    }

    private val shards = Array(numberOfShards, { x -> Shard("$name-shard-$x-", indexFactory) })
    private val size = shards.size.toLong()
    private val processor = QueryProcessor(this, store)

    override fun add(triple : Triple) {
        if(log.isTraceEnabled) log.trace("Adding $triple to store")
        store.add(triple)

        val shard = Math.abs(triple.hash % size).toInt()
        if(log.isTraceEnabled) log.trace("Adding $triple to shard: $shard")
        shards[shard].add(triple.hashTriple)

    }

    override fun get(indexType: IndexType, triple: HashTriple) : Set<HashTriple> {
        val set = HashSet<HashTriple>()
        shards.forEach { it.get(indexType, triple).triples.forEach { x -> set.add(x) } }
        return set
    }

    override fun bulkAdd(triples: Array<Triple>) = triples.forEach { i -> add(i) }
    override fun query(query: Query) = processor.process(query)
    override fun size() : Long = shards.fold(0L)  { x, y -> x + y.size() }
}
