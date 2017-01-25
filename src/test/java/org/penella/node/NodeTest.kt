package org.penella.node

import org.junit.Assert
import org.junit.Test
import org.penella.index.bstree.BSTreeIndexFactory
import org.penella.messages.CreateDB
import org.penella.messages.ListDB
import org.penella.messages.Status
import org.penella.node.Node
import org.penella.store.BSTreeStore

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
 * Created by alisle on 12/12/16.
 */
class NodeTest {

    @Test
    fun testCreateDB() {
        val store = BSTreeStore(455L)
        val indexFactory = BSTreeIndexFactory()
        val node = Node(store, indexFactory)
        val createMsg = CreateDB("Test", 100)

        val response = node.createDB(createMsg)

        Assert.assertEquals(response.status, Status.SUCESSFUL)

        val badResponse = node.createDB(createMsg)
        Assert.assertEquals(badResponse.status, Status.FAILED)
    }

    @Test
    fun testListDB() {
        val store = BSTreeStore(455L)
        val indexFactory = BSTreeIndexFactory()
        val node = Node(store, indexFactory)
        val names = arrayListOf("Test 1", "Test 2", "Test 3", "Test 4")

        names.forEach { x -> node.createDB(CreateDB(x, 100)) }

        val dbs = node.listDB(ListDB()).names

        names.forEach { x -> Assert.assertTrue(dbs.contains(x)) }
    }
}