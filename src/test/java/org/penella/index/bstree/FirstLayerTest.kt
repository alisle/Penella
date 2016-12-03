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
 * Created by alisle on 11/28/16.
 */

class FirstLayerTest {
    fun addLongs(layer: FirstLayer) {
        layer.add(1L, 2L, 3L)
        layer.add(11L, 12L, 13L)
        layer.add(21L, 22L, 23L)
        layer.add(31L, 32L, 33L)
        layer.add(41L, 42L, 43L)
        layer.add(51L, 52L, 53L)
        layer.add(61L, 62L, 63L)
    }

    @Test
    fun testSubjPropObjAddandGet() {
        val layer = FirstLayer(IndexType.SPO)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L
            val third = x + 3L

            for( triples in listOf(layer.get(first, second, third), layer.get(first, second), layer.get(first)) ) {
                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertEquals(triple.hashSubject, first)
                Assert.assertEquals(triple.hashProperty, second)
                Assert.assertEquals(triple.hashObj, third)
            }
        }


    }

    @Test
    fun testSubjObjAddandGet() {
        val layer = FirstLayer(IndexType.SO)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L
            val third = x + 3L

            for( triples in listOf(layer.get(first, second, third), layer.get(first, second), layer.get(first)) ) {
                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertEquals(triple.hashSubject, first)
                Assert.assertEquals(triple.hashObj, second)
                Assert.assertEquals(triple.hashProperty, third)
            }
        }

    }

    @Test
    fun testPropObjAddandGet() {
        val layer = FirstLayer(IndexType.PO)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L
            val third = x + 3L

            for( triples in listOf(layer.get(first, second, third), layer.get(first, second), layer.get(first)) ) {

                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertEquals(triple.hashProperty, first)
                Assert.assertEquals(triple.hashObj, second)
                Assert.assertEquals(triple.hashSubject, third)
            }
        }

    }


    @Test
    fun testObjAddandGet() {
        val layer = FirstLayer(IndexType.O)
        addLongs(layer)

        for(x in 0L..60L step 10) {
            val first = x + 1L
            val second = x + 2L
            val third = x + 3L

            for( triples in listOf(layer.get(first, second, third), layer.get(first, second), layer.get(first)) ) {
                Assert.assertEquals(triples.size, 1)

                val triple = triples[0]

                Assert.assertEquals(triple.hashObj, first)
                Assert.assertEquals(triple.hashSubject, second)
                Assert.assertEquals(triple.hashProperty, third)
            }
        }
    }

    @Test
    fun testMiss() {
        val layer = FirstLayer(IndexType.SO)
        addLongs(layer)
        assertEquals(layer.get(1000L).size, 0)
        assertEquals(layer.get(1L, 2000L).size, 0)
        assertEquals(layer.get(1, 2L, 3000L).size, 0)
    }


    @Test
    fun testAll() {
        val layer = FirstLayer(IndexType.SPO)
        addLongs(layer)
        val triples = layer.all()

        Assert.assertEquals(triples.size, 7)

        Assert.assertEquals(triples[0].hashSubject, 1L)
        Assert.assertEquals(triples[0].hashProperty, 2L)
        Assert.assertEquals(triples[0].hashObj, 3L)

        Assert.assertEquals(triples[1].hashSubject, 11L)
        Assert.assertEquals(triples[1].hashProperty, 12L)
        Assert.assertEquals(triples[1].hashObj, 13L)

        Assert.assertEquals(triples[2].hashSubject, 21L)
        Assert.assertEquals(triples[2].hashProperty, 22L)
        Assert.assertEquals(triples[2].hashObj, 23L)

        Assert.assertEquals(triples[3].hashSubject, 31L)
        Assert.assertEquals(triples[3].hashProperty, 32L)
        Assert.assertEquals(triples[3].hashObj, 33L)

        Assert.assertEquals(triples[4].hashSubject, 41L)
        Assert.assertEquals(triples[4].hashProperty, 42L)
        Assert.assertEquals(triples[4].hashObj, 43L)

        Assert.assertEquals(triples[5].hashSubject, 51L)
        Assert.assertEquals(triples[5].hashProperty, 52L)
        Assert.assertEquals(triples[5].hashObj, 53L)

        Assert.assertEquals(triples[6].hashSubject, 61L)
        Assert.assertEquals(triples[6].hashProperty, 62L)
        Assert.assertEquals(triples[6].hashObj, 63L)

    }
}