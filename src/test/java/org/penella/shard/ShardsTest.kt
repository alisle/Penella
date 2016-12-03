package org.penella.shard

import org.junit.Test
import org.junit.Assert

import org.penella.index.bstree.BSTreeIndexFactory
import org.penella.shards.Shard
import org.penella.structures.triples.HashTriple

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
 * Created by alisle on 11/30/16.
 */
class ShardsTest {
    @Test
    fun testAdd() {
        val shard = Shard(BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, x, x)) }
        Assert.assertEquals(shard.size(), 100)
    }

    @Test
    fun testQuery() {
        // TODO: Need to make the query when we actually have the query.
    }



}