package org.penella.index.bstree

import org.penella.index.bstree.BSTreeIndexFactory
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
 * Created by alisle on 11/18/16.
 */
class FirstLayer(val indexType: IndexType) {
    val firstType = IndexType.getOrder(indexType).first
    private val layer : BSTree<Long, MiddleLayer> = BSTree()

    companion object  {
        private val log: Logger = LoggerFactory.getLogger(FirstLayer::class.java)
    }

    fun add(firstValue: Long, secondValue: Long, thirdValue: Long) {
        val tree = layer.addOrGet(firstValue, MiddleLayer(indexType))
        tree.add(secondValue, thirdValue)
    }

    fun get(firstValue: Long) : Array<HashTriple> {
        val lookup = layer.get(firstValue)
        if(lookup == null) { return emptyArray() }
        return HashTriple.create(firstType, firstValue, lookup.all())
    }

    fun get(firstValue: Long, secondValue: Long) : Array<HashTriple> {
        val lookup = layer.get(firstValue)
        if(lookup == null) { return emptyArray() }

        return HashTriple.create(firstType, firstValue, lookup.get(secondValue))
    }

    fun get(firstValue: Long, secondValue: Long, thirdValue: Long) : Array<HashTriple> {
        val lookup = layer.get(firstValue)
        if(lookup == null) { return emptyArray() }

        return HashTriple.create(firstType, firstValue, lookup.get(secondValue, thirdValue))

    }

    fun all() : Array<HashTriple> {
        return layer.all().map{ x-> HashTriple.create(firstType, x.first, x.second.all()).toMutableList() }.flatten().toTypedArray()
    }
}