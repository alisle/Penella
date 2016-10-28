package org.penella

import org.junit.Test
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
    @Test
    fun testHash() {
        val first  : Triple = Triple("Subject", "Object", "Property")
        val second : Triple = Triple("Subject 2", "Object 2", "Property 2")
        val third  : Triple = Triple("Subject", "Object", "Property")

        assertEquals(first.hash, third.hash)
        assertNotEquals(first.hash, second.hash)

        assertEquals(first.hashSubject, third.hashSubject)
        assertEquals(first.hashObj, third.hashObj)
        assertEquals(first.hashProperty, third.hashProperty)


        assertNotEquals(first.hashSubject, second.hashSubject)
        assertNotEquals(first.hashObj, second.hashObj)
        assertNotEquals(first.hashProperty, second.hashProperty)
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
}