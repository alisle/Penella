package org.penella.index.bstree

import org.junit.Assert
import org.junit.Test
import org.penella.index.IndexType
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
 * Created by alisle on 11/21/16.
 */
class MiddleLayerTest {

    fun addLongs(layer : MiddleLayer) {
        layer.add(1L, 2L)
        layer.add(11L, 12L)
        layer.add(21L, 22L)
        layer.add(31L, 32L)
        layer.add(41L, 42L)
        layer.add(51L, 52L)
        layer.add(61L, 62L)
    }

    @Test
    fun testAll() {
        val layer = MiddleLayer(IndexType.SPO)
        addLongs(layer)

        val triples = layer.all()

        Assert.assertEquals(triples.size, 7)

        Assert.assertEquals(triples[0].hashProperty, 1L)
        Assert.assertEquals(triples[0].hashObj, 2L)

        Assert.assertEquals(triples[1].hashProperty, 11L)
        Assert.assertEquals(triples[1].hashObj, 12L)

        Assert.assertEquals(triples[2].hashProperty, 21L)
        Assert.assertEquals(triples[2].hashObj, 22L)

        Assert.assertEquals(triples[3].hashProperty, 31L)
        Assert.assertEquals(triples[3].hashObj, 32L)

        Assert.assertEquals(triples[4].hashProperty, 41L)
        Assert.assertEquals(triples[4].hashObj, 42L)

        Assert.assertEquals(triples[5].hashProperty, 51L)
        Assert.assertEquals(triples[5].hashObj, 52L)

        Assert.assertEquals(triples[6].hashProperty, 61L)
        Assert.assertEquals(triples[6].hashObj, 62L)

    }
    @Test
    fun testSubjPropObjAddandGet() {
        val layer = MiddleLayer(IndexType.SPO)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L

            for( triples in listOf(layer.get(first, second), layer.get(first)) ) {
                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertNotEquals(triple.hashObj,  first)
                Assert.assertEquals(triple.hashProperty, first)
                Assert.assertEquals(triple.hashObj, second)
            }
        }


    }

    @Test
    fun testMiss() {
        val layer = MiddleLayer(IndexType.SO)
        addLongs(layer)
        assertEquals(layer.get(1000L).size, 0)
        assertEquals(layer.get(1L, 2000L).size, 0)
    }

    @Test
    fun testSubjObjAddandGet() {
        val layer = MiddleLayer(IndexType.SO)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L

            for( triples in listOf(layer.get(first, second), layer.get(first)) ) {
                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertNotEquals(triple.hashSubject, first)
                Assert.assertEquals(triple.hashObj, first)
                Assert.assertEquals(triple.hashProperty, second)
            }
        }

    }

    @Test
    fun testPropObjAddandGet() {
        val layer = MiddleLayer(IndexType.PO)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L

            for( triples in listOf(layer.get(first, second), layer.get(first)) ) {

                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertNotEquals(triple.hashProperty, first)
                Assert.assertEquals(triple.hashObj, first)
                Assert.assertEquals(triple.hashSubject, second)
            }
        }

    }


    @Test
    fun testObjAddandGet() {
        val layer = MiddleLayer(IndexType.O)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L

            for( triples in listOf(layer.get(first, second), layer.get(first)) ) {
                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertNotEquals(triple.hashObj, first)
                Assert.assertEquals(triple.hashSubject, first)
                Assert.assertEquals(triple.hashProperty, second)
            }
        }

    }

}