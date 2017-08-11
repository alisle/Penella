package org.penella.structures.triples

import org.junit.Assert
import org.junit.Test

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
class HashTripleTest {

    @Test
    fun testSimpleConstructor() {
        val triple = HashTriple(1L, 2L, 3L)
        Assert.assertEquals(triple.hashSubject, 1L)
        Assert.assertEquals(triple.hashProperty, 2L)
        Assert.assertEquals(triple.hashObj, 3L)

    }

    @Test
    fun testHashCode() {
        val triple = HashTriple(1L, 2L, 3L)
        val triple2 = HashTriple(1L, 2L, 3L)
        val triple3 = HashTriple(2L, 1L, 3L)

        Assert.assertEquals(triple.hashCode(), triple2.hashCode())
        Assert.assertNotEquals(triple3.hashCode(), triple.hashCode())
    }

    @Test
    fun testEquals() {
        val triple = HashTriple(1L, 2L, 3L)
        val triple2 = HashTriple(1L, 2L, 3L)
        val triple3 = HashTriple(2L, 1L, 3L)

        Assert.assertTrue(triple.equals(triple2))
        Assert.assertFalse(triple.equals(triple3))
    }

    @Test
    fun testSeed() {
        Assert.assertEquals(HashTriple.seed, HashTriple.seed)
    }

    @Test
    fun testSingleCreate() {
        val subTriple = HashTriple.create(TripleType.SUBJECT, 1L)
        val propTriple = HashTriple.create(TripleType.PROPERTY, 2L)
        val objTriple = HashTriple.create(TripleType.OBJECT, 3L)

        Assert.assertEquals(subTriple.size, 1)
        Assert.assertEquals(propTriple.size, 1)
        Assert.assertEquals(objTriple.size, 1)


        Assert.assertEquals(subTriple[0].hashSubject, 1L)
        Assert.assertNotEquals(subTriple[0].hashObj, 1L)
        Assert.assertNotEquals(subTriple[0].hashProperty, 1L)

        Assert.assertNotEquals(propTriple[0].hashSubject, 2L)
        Assert.assertNotEquals(propTriple[0].hashObj, 2L)
        Assert.assertEquals(propTriple[0].hashProperty, 2L)

        Assert.assertNotEquals(objTriple[0].hashSubject, 3L)
        Assert.assertEquals(objTriple[0].hashObj, 3L)
        Assert.assertNotEquals(objTriple[0].hashProperty, 3L)
    }

    @Test
    fun testSingleListCreate() {
        val list = listOf(1L, 2L, 3L)
        val subjTriples = HashTriple.create(TripleType.SUBJECT, list)
        val propTriples = HashTriple.create(TripleType.PROPERTY, list)
        val objTriples = HashTriple.create(TripleType.OBJECT, list)

        for(x in 1L..3L) {
            val triple = subjTriples[x.toInt() - 1]

            Assert.assertEquals(triple.hashSubject, x)
            Assert.assertNotEquals(triple.hashObj, x)
            Assert.assertNotEquals(triple.hashProperty, x)
        }

        for(x in 1L..3L) {
            val triple = propTriples[x.toInt() - 1]

            Assert.assertNotEquals(triple.hashSubject, x)
            Assert.assertNotEquals(triple.hashObj, x)
            Assert.assertEquals(triple.hashProperty, x)
        }

        for(x in 1L..3L) {
            val triple = objTriples[x.toInt() - 1]

            Assert.assertNotEquals(triple.hashSubject, x)
            Assert.assertEquals(triple.hashObj, x)
            Assert.assertNotEquals(triple.hashProperty, x)
        }

    }

    @Test
    fun testHashTripleFromHashTripleList() {
        val list = listOf(1L, 2L, 3L)
        val subjTriples = HashTriple.create(TripleType.SUBJECT, list)
        val subjPropTriples = HashTriple.create(TripleType.PROPERTY, 4L, subjTriples)
        val subjObjTriples = HashTriple.create(TripleType.OBJECT, 5L, subjTriples)
        val subjPropObj = HashTriple.create(TripleType.OBJECT, 6L, subjPropTriples)

        val objTriples = HashTriple.create(TripleType.OBJECT, list)
        val objSubjTriples = HashTriple.create(TripleType.SUBJECT, 4L, objTriples)

        Assert.assertEquals(3, objSubjTriples.size)
        Assert.assertEquals(3, subjPropTriples.size)
        Assert.assertEquals(3, subjObjTriples.size)
        Assert.assertEquals(3, subjPropObj.size)

        for(x in 1L..3L) {
            var triple = subjPropTriples[x.toInt() -1]

            Assert.assertEquals(x, triple.hashSubject)
            Assert.assertEquals(triple.hashObj, 0L)
            Assert.assertEquals(triple.hashProperty, 4L)

            triple = subjObjTriples[x.toInt() -1]
            Assert.assertEquals(x, triple.hashSubject)
            Assert.assertEquals(triple.hashObj, 5L)
            Assert.assertEquals(triple.hashProperty, 0L)

            triple = subjPropObj[x.toInt() -1]
            Assert.assertEquals(x, triple.hashSubject)
            Assert.assertEquals(triple.hashObj, 6L)
            Assert.assertEquals(triple.hashProperty, 4L)
        }

        for(x in 1L..3L) {
            var triple = objSubjTriples[x.toInt() -1]
            Assert.assertEquals(x, triple.hashObj)
        }

    }


}