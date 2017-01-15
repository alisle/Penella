package org.penella.index

import org.junit.Test
import org.penella.structures.triples.HashTriple
import kotlin.test.assertEquals

/**
 * Created by alisle on 12/30/16.
 */

class IndexTypeTest {

    @Test
    fun testGetIndexTypeTriple() {
        var indextype = IndexType.getIndex(HashTriple(1L, 1L, 1L))
        assertEquals(indextype, IndexType.SPO)

        indextype = IndexType.getIndex(HashTriple(1L, 1L, 0L))
        assertEquals(indextype, IndexType.SPO)

        indextype = IndexType.getIndex(HashTriple(1L, 0L, 1L))
        assertEquals(indextype, IndexType.SO)

        indextype = IndexType.getIndex(HashTriple(1L, 0L, 0L))
        assertEquals(indextype, IndexType.SPO)

        indextype = IndexType.getIndex(HashTriple(0L, 1L, 1L))
        assertEquals(indextype, IndexType.PO)

        indextype = IndexType.getIndex(HashTriple(0L, 1L, 0L))
        assertEquals(indextype, IndexType.PO)

        indextype = IndexType.getIndex(HashTriple(0L, 0L, 1L))
        assertEquals(indextype, IndexType.O)

        indextype = IndexType.getIndex(HashTriple(0L, 0L, 0L))
        assertEquals(indextype, IndexType.SPO)
    }
    @Test
    fun testGetIndexTypeLong() {
        var indextype = IndexType.getIndex(1L, 1L, 1L)
        assertEquals(indextype, IndexType.SPO)

        indextype = IndexType.getIndex(1L, 1L, 0L)
        assertEquals(indextype, IndexType.SPO)

        indextype = IndexType.getIndex(1L, 0L, 1L)
        assertEquals(indextype, IndexType.SO)

        indextype = IndexType.getIndex(1L, 0L, 0L)
        assertEquals(indextype, IndexType.SPO)

        indextype = IndexType.getIndex(0L, 1L, 1L)
        assertEquals(indextype, IndexType.PO)

        indextype = IndexType.getIndex(0L, 1L, 0L)
        assertEquals(indextype, IndexType.PO)

        indextype = IndexType.getIndex(0L, 0L, 1L)
        assertEquals(indextype, IndexType.O)

        indextype = IndexType.getIndex(0L, 0L, 0L)
        assertEquals(indextype, IndexType.SPO)
    }
}