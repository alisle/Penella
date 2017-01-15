package org.penella.shard

import org.junit.Test
import org.junit.Assert
import org.penella.index.IndexType

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
    fun testGetS() {
        val shard = Shard(BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, x, x)) }
        val results = shard.get(IndexType.SPO, HashTriple(1000L, 0L, 0L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, it, it))) }
    }

    @Test
    fun testGetP() {
        val shard = Shard(BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, 1000L, x)) }
        val results = shard.get(IndexType.PO, HashTriple(0L, 1000L, 0L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, 1000L, it))) }
    }

    @Test
    fun testGetO() {
        val shard = Shard(BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, x, 1000L)) }
        val results = shard.get(IndexType.O, HashTriple(0L, 0L, 1000L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, it, 1000L))) }
    }


    @Test
    fun testGetSPO() {
        val shard = Shard(BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, 2000L, x)) }
        val results = shard.get(IndexType.SPO, HashTriple(1000L, 2000L, 0L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, 2000L, it))) }
    }


    @Test
    fun testGetPO() {
        val shard = Shard(BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(x, 2000L, 1000L)) }
        val results = shard.get(IndexType.PO, HashTriple(0L, 2000L, 1000L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(it, 2000L, 1000L))) }
    }


    @Test
    fun testGetSO() {
        val shard = Shard(BSTreeIndexFactory())
        (0L until 100L).forEach { x -> shard.add(HashTriple(1000L, x, 2000L)) }
        val results = shard.get(IndexType.SO, HashTriple(1000L, 0L, 2000L))
        Assert.assertEquals(results.triples.size, 100)

        (0L until 100L).forEach { Assert.assertTrue(results.triples.contains(HashTriple(1000L, it, 2000L))) }
    }
}