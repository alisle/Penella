package org.penella.index.bstree

import org.junit.Assert
import org.junit.Test
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.TripleType
import kotlin.test.assertEquals

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
 * Created by alisle on 11/30/16.
 */
class SubjectPropertyObjectBSTreeIndexTest {
    @Test
    fun testAddAndGetSingle() {
        val index = SubjectPropertyObjectBSTreeIndex()
        index.add(HashTriple(1L, 2L, 3L))
        index.add(HashTriple(1L, 2L, 31L))
        index.add(HashTriple(1L, 21L, 3L))
        index.add(HashTriple(1L, 21L, 31L))
        index.add(HashTriple(11L, 2L, 3L))
        index.add(HashTriple(11L, 2L, 31L))

        val triples = index.get(TripleType.SUBJECT, 1L).triples
        assertEquals(triples.size, 4)
    }

    @Test
    fun testAddAndGetMultiple() {
        val index = SubjectPropertyObjectBSTreeIndex()
        index.add(HashTriple(1L, 2L, 3L))
        index.add(HashTriple(1L, 2L, 31L))
        index.add(HashTriple(1L, 21L, 3L))
        index.add(HashTriple(1L, 21L, 31L))
        index.add(HashTriple(11L, 2L, 3L))
        index.add(HashTriple(11L, 2L, 31L))
        val triples = index.get(TripleType.SUBJECT, TripleType.PROPERTY, 1L, 2L).triples
        assertEquals(triples.size, 2)


    }

    @Test
    fun testWrongAdd() {
        val index = SubjectPropertyObjectBSTreeIndex()
        index.add(HashTriple(1L, 2L, 3L))
        index.add(HashTriple(11L, 12L, 13L))
        index.add(HashTriple(21L, 22L, 23L))

        try {
            val tiples = index.get(TripleType.PROPERTY, 1L).triples;
        } catch( e: IncorrectIndexRequest) {
            return
        }

        try {
            val tiples = index.get(TripleType.PROPERTY, TripleType.SUBJECT, 1L, 2L).triples;
        } catch( e: IncorrectIndexRequest) {
            return
        }

        Assert.assertFalse(true)
    }
}