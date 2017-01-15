package org.penella.query

import org.penella.structures.triples.Triple;

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.messages.AddTriple
import org.penella.messages.BulkAddTriples
import org.penella.messages.RawQuery

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
 * Created by alisle on 12/22/16.
 */


class QueriesTest {
    @Before
    fun setup() {
        Json.mapper.registerModule(KotlinModule())
    }

    @Test
    fun testAddTriple() {
        val add = AddTriple(Triple("Subject", "Property", "Object"))
        val json = Json.encode(add)
        val newAdd = Json.decodeValue(json, AddTriple::class.java)

        Assert.assertEquals(add, newAdd)
    }

    @Test
    fun testBulkAddTriple() {
        val triples = Array(5, { i -> Triple("Subject $i", "Property $i", "Object $i") })
        val bulkAdd = BulkAddTriples(triples)
        val json = Json.encode(bulkAdd)
        val newBulkdAdd = Json.decodeValue(json, BulkAddTriples::class.java)
        Assert.assertArrayEquals(newBulkdAdd.triples, bulkAdd.triples)
    }

    @Test
    fun testQuery() {
        var outputs = Array(1, { i -> "?Test-$i" })
        val variables = Array(5, { i -> "?Test-$i" })
        val triples = Array(5, { i -> Triple("Subject $i", "Property $i", "Object $i") })
        val query = RawQuery(outputs, variables, triples)
        val json = Json.encode(query)
        val newQuery = Json.decodeValue(json, RawQuery::class.java)

        Assert.assertArrayEquals(outputs, newQuery.outputs)
        Assert.assertArrayEquals(variables, newQuery.variables)
        Assert.assertArrayEquals(triples, newQuery.triples)
    }
}