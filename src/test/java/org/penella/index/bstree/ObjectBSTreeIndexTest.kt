package org.penella.index.bstree

import org.junit.Assert
import org.junit.Test
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.TripleType
import kotlin.test.assertEquals

/**
 * Created by alisle on 11/29/16.
 */
class ObjectBSTreeIndexTest {


    @Test
    fun testAddAndGetSingle() {
        val index = ObjectBSTreeIndex()
        index.add(HashTriple(1L, 2L, 3L))
        index.add(HashTriple(1L, 2L, 31L))
        index.add(HashTriple(1L, 21L, 3L))
        index.add(HashTriple(1L, 21L, 31L))
        index.add(HashTriple(11L, 2L, 3L))
        index.add(HashTriple(11L, 2L, 31L))

        val triples = index.get(TripleType.OBJECT, 3L).triples
        assertEquals(triples.size, 3)
    }

    @Test
    fun testAddAndGetMultiple() {
        val index = ObjectBSTreeIndex()
        index.add(HashTriple(1L, 2L, 3L))
        index.add(HashTriple(11L, 12L, 13L))
        index.add(HashTriple(21L, 22L, 23L))

        try {
            index.get(TripleType.OBJECT, TripleType.PROPERTY, 1L, 2L).triples

        } catch (e : InvalidIndexRequest) {
            return
        }


        Assert.assertFalse(true)
    }

    @Test
    fun testWrongAdd() {
        val index = ObjectBSTreeIndex()
        index.add(HashTriple(1L, 2L, 3L))
        index.add(HashTriple(11L, 12L, 13L))
        index.add(HashTriple(21L, 22L, 23L))

        try {
            index.get(TripleType.PROPERTY, 1L).triples;
        } catch( e: IncorrectIndexRequest) {
            return
        }

        Assert.assertFalse(true)
    }

}