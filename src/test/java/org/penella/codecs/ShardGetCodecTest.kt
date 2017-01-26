package org.penella.codecs

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.index.IndexType
import org.penella.messages.ShardGet
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
 *
 * Created by alisle on 1/26/17.
 */
class ShardGetCodecTest {
    @Before
    fun setUp() {
        Json.mapper.registerModule(KotlinModule())
    }

    @Test
    fun testName() {
        val codec = ShardGetCodec()
        Assert.assertEquals(codec.name(), "ShardGet")
    }


    @Test
    fun testEncodeDecode() {
        val codec = ShardGetCodec()
        val buffer = Buffer.buffer()
        val value = ShardGet(IndexType.SO, HashTriple(1L, 10L, 100L))

        codec.encodeToWire(buffer, value)
        val newValue = codec.decodeFromWire(0, buffer)

        Assert.assertEquals(value, newValue)
    }

    @Test
    fun testTransform() {
        val codec = ShardGetCodec()
        val value = ShardGet(IndexType.SO, HashTriple(1L, 10L, 100L))
        val newValue = codec.transform(value)

        Assert.assertEquals(value, newValue)
    }
}