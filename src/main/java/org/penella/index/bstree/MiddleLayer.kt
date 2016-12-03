package org.penella.index.bstree

import org.penella.index.IndexType
import org.penella.structures.BSTree
import org.penella.structures.triples.HashTriple
import org.slf4j.LoggerFactory
import org.slf4j.Logger


/**
 * Created by alisle on 11/15/16.
 */
class MiddleLayer(val indexType: IndexType) {
    val secondType = IndexType.getOrder(indexType).second
    private val layer : BSTree<Long, LastLayer> = BSTree()

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MiddleLayer::class.java)
    }

    fun add(secondValue: Long, thirdValue: Long) {
        val tree = layer.addOrGet(secondValue, LastLayer(indexType))
        tree.add(thirdValue)
    }

    fun get(secondValue: Long) : Array<HashTriple> {
        val lookup = layer.get(secondValue)
        if(lookup == null) { return emptyArray() }
        return HashTriple.create(secondType, secondValue, lookup.all())
    }

    fun get(secondValue: Long, thirdValue: Long) : Array<HashTriple> {
        val lookup = layer.get(secondValue)
        if(lookup == null) { return emptyArray() }
        return HashTriple.create(secondType, secondValue, lookup.get(thirdValue))
    }

    fun all() : Array<HashTriple> {
        return layer.all().map { x -> HashTriple.create(secondType, x.first, x.second.all()).toMutableList() }.flatten().toTypedArray()
    }
}