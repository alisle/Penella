package org.penella.store

import org.junit.Test
import org.penella.structures.triples.Triple
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
 * Created by alisle on 10/18/16.
 */
abstract class StoreTest {
    abstract fun createStore() : IStore
    abstract fun storeType() :  String

    companion object {
        val log : Logger = LoggerFactory.getLogger(StoreTest::class.java)
    }

    @Test
    fun testStringInsert() {
        val store  = createStore()
        val hash = store.add("Hello World")
        assertEquals("Hello World", store.get(hash))
    }

    @Test
    fun testTripleInsert() {
        val store = createStore()
        val triple = Triple("Subject", "Property", "Object")
        store.add(triple)

        assertEquals(triple.subject, store.get(triple.hashTriple.hashSubject))
        assertEquals(triple.property, store.get(triple.hashTriple.hashProperty))
        assertEquals(triple.obj, store.get(triple.hashTriple.hashObj))
    }

    @Test
    fun testGetTriple() {
        val store = createStore()
        val triple = Triple("Subject", "Property", "Object")
        store.add(triple)

        val newTriple = store.get(triple.hashTriple)
        assertEquals(triple, newTriple)

    }

    @Test
    fun testTripleInsertPerformance() {
        val max = 1000
        var start = System.currentTimeMillis()
        var store = createStore()

        log.debug("Starting String Insert Test")
        for(x  in 1L..max) {
            val string = "Test " + x
            store.add(Triple("Subject" + string, "Property", "Object"))
        }

        var finish = (System.currentTimeMillis() - start)
        var average : Double = max / (finish / 1000.0)

        log.warn("Finished in " + finish / 1000 + " secs")
        log.warn("Average insert time: " + average + " recs / sec")
    }

    @Test
    fun testStringInsertPerformance() {
        val max = 10000
        var start = System.currentTimeMillis()
        var store = createStore()

        log.debug("Starting String Insert Test")
        for(x  in 1L..max) {
            val string = "Test " + x
            store.add(string)
        }

        var finish = (System.currentTimeMillis() - start)
        var average : Double = max / (finish / 1000.0)

        log.warn("Finished in " + finish / 1000 + " secs")
        log.warn("Average insert time: " + average + " recs / sec")

        start = System.currentTimeMillis()
        log.warn("Retrieving " + max + " instances")
        for( x in 1L..max) {
            val string = "Test " + x
            val hash = store.generateHash(string)
            val oldString = store.get(hash)
            assertEquals(string, oldString)
        }

        finish = (System.currentTimeMillis() - start)
        average = max / (finish / 1000.0)

        log.warn("Finished in " + finish / 1000 + " secs")
        log.warn("Average Fetch time: " + average + " recs / sec")


    }
}