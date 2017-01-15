package org.penella.structures.triples

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.structures.triples.Triple
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

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
 * Created by alisle on 10/7/16.
 */
class TripleTest {

    @Before
    fun setup() {
        Json.mapper.registerModule(KotlinModule())
    }

    @Test
    fun testHash() {
        val first  : Triple = Triple("Subject", "Object", "Property")
        val second : Triple = Triple("Subject 2", "Object 2", "Property 2")
        val third  : Triple = Triple("Subject", "Object", "Property")

        assertEquals(first.hash, third.hash)
        assertNotEquals(first.hash, second.hash)

        assertEquals(first.hashTriple.hashSubject, third.hashTriple.hashSubject)
        assertEquals(first.hashTriple.hashObj, third.hashTriple.hashObj)
        assertEquals(first.hashTriple.hashProperty, third.hashTriple.hashProperty)


        assertNotEquals(first.hashTriple.hashSubject, second.hashTriple.hashSubject)
        assertNotEquals(first.hashTriple.hashObj, second.hashTriple.hashObj)
        assertNotEquals(first.hashTriple.hashProperty, second.hashTriple.hashProperty)
    }

    @Test
    fun testGetHash() {
        val first = "Hello World!"
        val second = "Hello World!"

        val firstHash = Triple.getHash(first)
        val secondHash = Triple.getHash(second)

        assertEquals(firstHash, secondHash)
    }


    @Test
    fun testAccessors() {
        val first  : Triple = Triple("Subject", "Property", "Object")

        assertEquals(first.subject, "Subject")
        assertEquals(first.obj, "Object")
        assertEquals(first.property, "Property")
    }

    @Test
    fun testEquals() {
        val first  : Triple = Triple("Subject", "Object", "Property")
        val second : Triple = Triple("Subject 2", "Object 2", "Property 2")
        val third  : Triple = Triple("Subject", "Object", "Property")

        assertTrue(first.equals(third))
        assertFalse(first.equals(second))
    }

    @Test
    fun testJSON() {

        val triple = Triple("Subject", "Property", "Object")
        val json = triple.toJSON()

        Assert.assertTrue(json.length > 0)

        val newTriple = Triple.fromJSON(json)

        Assert.assertEquals(newTriple, triple)
    }

    @Test
    fun testIncompleteJSON() {
        Json.mapper.registerModule(KotlinModule())
        val json = "{\"subject\":\"Subject\",\"property\":\"Property\",\"object\":\"Object\"}";
        val triple = Triple.fromJSON(json)
        Assert.assertEquals(triple, Triple("Subject", "Property", "Object"));
    }
}