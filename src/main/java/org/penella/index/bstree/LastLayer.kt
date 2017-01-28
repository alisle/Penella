package org.penella.index.bstree

import org.penella.index.IndexType
import org.penella.structures.BSTree
import org.penella.structures.triples.HashTriple
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
 * Created by alisle on 11/14/16.
 */

class LastLayer(indexType: IndexType ) {
    val type = IndexType.getOrder(indexType).third

    companion object {
        private val log: Logger = LoggerFactory.getLogger(LastLayer::class.java)
    }

    private val layer : BSTree<Long, Long> = BSTree()

    fun add(value: Long) = layer.add(value, value)

    fun get(value: Long) : Array<HashTriple> {
        if(log.isTraceEnabled) log.trace("Looking up $value")

        val lookup = layer.get(value)
        when(lookup) {
            null -> return emptyArray()
            else -> return HashTriple.create(type, lookup)
        }
    }

    fun all() : Array<HashTriple> {
        return HashTriple.create(type, layer.allPayloads())
    }



}