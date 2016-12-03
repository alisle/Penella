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
 * Created by alisle on 11/18/16.
 */
class LastLayerTest() {
    fun addLongs(layer: LastLayer) {
        layer.add(1L)
        layer.add(2L)
        layer.add(3L)
        layer.add(4L)
        layer.add(5L)
        layer.add(6L)
    }

    fun checkLongs(layer: LastLayer, subjEqual : Boolean, propEqual: Boolean, objEqual: Boolean) {
        for(x in 1L ..6L) {
            Assert.assertEquals(layer.get(x).size, 1)
            val triple  = layer.get(x)[0]

            if(subjEqual) { Assert.assertEquals(triple.hashSubject, x) } else { Assert.assertNotEquals(triple.hashSubject, x) }
            if(propEqual) { Assert.assertEquals(triple.hashProperty, x) } else { Assert.assertNotEquals(triple.hashProperty, x) }
            if(objEqual) { Assert.assertEquals(triple.hashObj, x) } else { Assert.assertNotEquals(triple.hashObj, x) }
        }
    }

    @Test
    fun testSubjPropObjAddandGet() {
        val layer = LastLayer(IndexType.SPO)
        addLongs(layer)
        checkLongs(layer, false, false, true)
    }

    @Test
    fun testSubjObjAddandGet() {
        val layer = LastLayer(IndexType.SO)
        addLongs(layer)
        checkLongs(layer, false, true, false)
    }

    @Test
    fun testObjAddandGet() {
        val layer = LastLayer(IndexType.O)
        addLongs(layer)
        checkLongs(layer, false, true, false)
    }

    @Test
    fun testMiss() {
        val layer = LastLayer(IndexType.SO)
        addLongs(layer)
        assertEquals(layer.get(1000L).size, 0)
    }


    @Test
    fun testPropObjAddandGet() {
        val layer = LastLayer(IndexType.PO)
        addLongs(layer)
        checkLongs(layer, true, false, false)
    }

    @Test
    fun testAll() {
        val layer = LastLayer(IndexType.PO)
        addLongs(layer)

        val all  = layer.all()
        Assert.assertEquals(all.size, 6)
        for(x in 1L..6L) {
            Assert.assertEquals(all[x.toInt() -1].hashSubject, x)
        }
    }

}