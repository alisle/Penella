package org.penella.database

import org.junit.Test
import org.junit.Assert

import org.penella.database.DatabaseImpl
import org.penella.database.IDatabase
import org.penella.index.bstree.BSTreeIndexFactory
import org.penella.store.BSTreeStore
import org.penella.structures.triples.Triple



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
 * Created by alisle on 12/2/16.
 */

class DatabaseImplTest {
    @Test
    fun testAdd() {
        val store = BSTreeStore(665445839L)
        val db : IDatabase = DatabaseImpl("Test DB", store, BSTreeIndexFactory(), 10)
        (0L until 10000L).forEach { x -> db.add(Triple("Subject " + x, "Property " + x, "Object " + x)) }

        Assert.assertEquals(10000L, db.size())
    }
}