package org.penella.structures.triples

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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
 */
class QueryTripleTest {
    @Test
    fun testStringConstructor() {
        val oneVariables = QueryTriple("?Hello", "World", "fubar")
        val twoVariable = QueryTriple("?Hello", "?World", "fubar")
        val threeVariable = QueryTriple("?Hello", "?World", "?fubar")

        assertEquals(oneVariables.variables.size, 1)
        assertEquals(twoVariable.variables.size, 2)
        assertEquals(threeVariable.variables.size, 3)

        assertEquals(oneVariables.variables[0], "?Hello")

        assertEquals(oneVariables.hashTriple.hashSubject, 0L)
        assertNotEquals(oneVariables.hashTriple.hashProperty, 0L)
        assertNotEquals(oneVariables.hashTriple.hashObj, 0L)

        assertEquals(twoVariable.variables[0], "?Hello")
        assertEquals(twoVariable.variables[1], "?World")

        assertEquals(twoVariable.hashTriple.hashSubject, 0L)
        assertEquals(twoVariable.hashTriple.hashProperty, 0L)
        assertNotEquals(twoVariable.hashTriple.hashObj, 0L)

        assertEquals(threeVariable.variables[0], "?Hello")
        assertEquals(threeVariable.variables[1], "?World")
        assertEquals(threeVariable.variables[2], "?fubar")

        assertEquals(threeVariable.hashTriple.hashSubject, 0L)
        assertEquals(threeVariable.hashTriple.hashProperty, 0L)
        assertEquals(threeVariable.hashTriple.hashObj, 0L)

    }

    @Test
    fun testGetType() {
        var queryTriple = QueryTriple("?Subject", "Property", "Obj")
        assertEquals(queryTriple.getType("?Subject"), TripleType.SUBJECT)
        assertEquals(queryTriple.getType("Property"), TripleType.NONE)
        assertEquals(queryTriple.getType("Obj"), TripleType.NONE)

        queryTriple = QueryTriple("Subject", "?Property", "Obj")
        assertEquals(queryTriple.getType("Subject"), TripleType.NONE)
        assertEquals(queryTriple.getType("?Property"), TripleType.PROPERTY)
        assertEquals(queryTriple.getType("Obj"), TripleType.NONE)

        queryTriple = QueryTriple("Subject", "Property", "?Obj")
        assertEquals(queryTriple.getType("Subject"), TripleType.NONE)
        assertEquals(queryTriple.getType("Property"), TripleType.NONE)
        assertEquals(queryTriple.getType("?Obj"), TripleType.OBJECT)

    }
    @Test
    fun testTripleConstructor() {
        val oneVariables = QueryTriple(Triple("?Hello", "World", "fubar"))
        val twoVariable = QueryTriple(Triple("?Hello", "?World", "fubar"))
        val threeVariable = QueryTriple(Triple("?Hello", "?World", "?fubar"))

        assertEquals(oneVariables.variables.size, 1)
        assertEquals(twoVariable.variables.size, 2)
        assertEquals(threeVariable.variables.size, 3)

        assertEquals(oneVariables.variables[0], "?Hello")

        assertEquals(oneVariables.hashTriple.hashSubject, 0L)
        assertNotEquals(oneVariables.hashTriple.hashProperty, 0L)
        assertNotEquals(oneVariables.hashTriple.hashObj, 0L)

        assertEquals(twoVariable.variables[0], "?Hello")
        assertEquals(twoVariable.variables[1], "?World")

        assertEquals(twoVariable.hashTriple.hashSubject, 0L)
        assertEquals(twoVariable.hashTriple.hashProperty, 0L)
        assertNotEquals(twoVariable.hashTriple.hashObj, 0L)

        assertEquals(threeVariable.variables[0], "?Hello")
        assertEquals(threeVariable.variables[1], "?World")
        assertEquals(threeVariable.variables[2], "?fubar")

        assertEquals(threeVariable.hashTriple.hashSubject, 0L)
        assertEquals(threeVariable.hashTriple.hashProperty, 0L)
        assertEquals(threeVariable.hashTriple.hashObj, 0L)
    }
}