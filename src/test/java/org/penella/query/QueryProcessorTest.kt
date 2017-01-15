package org.penella.query

import org.junit.Assert
import org.junit.Test
import org.penella.database.DatabaseImpl
import org.penella.database.IDatabase
import org.penella.index.bstree.BSTreeIndexFactory
import org.penella.messages.AddTriple
import org.penella.messages.BulkAddTriples
import org.penella.messages.RawQuery
import org.penella.store.BSTreeStore
import org.penella.structures.triples.Triple
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
 * Created by alisle on 1/3/17.
 */
class QueryProcessorTest {
    @Test
    fun testSimple() {
        val store = BSTreeStore(665445839L)
        val db : IDatabase = DatabaseImpl("Test DB", store, BSTreeIndexFactory(), 10)
        val processor = QueryProcessor(db, store)
        val testTriples = ( 0 to 5 ).toList().map { Triple("Test-Subject", "Property", "Object-$it") };

        testTriples.forEach { db.handle(AddTriple(it)) }
        db.handle(BulkAddTriples((0L until 10000L).map { x -> Triple("Subject " + x, "Property " + x, "Object " + x) }.toTypedArray()))

        val raw = RawQuery(arrayOf("?Subject"), arrayOf("?Subject", "?Object"), arrayOf(Triple("?Subject", "Property", "?Object")))
        val results = processor.process(raw)
        results.forEach { x -> Assert.assertTrue(testTriples.contains(x)) }
    }
}