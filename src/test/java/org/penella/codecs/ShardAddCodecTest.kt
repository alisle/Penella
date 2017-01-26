package org.penella.codecs

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.messages.ShardAdd
import org.penella.messages.ShardSizeResponse
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
 * Created by alisle on 1/26/17.
 */
class ShardAddCodecTest {
    @Before
    fun setUp() {
        Json.mapper.registerModule(KotlinModule())
    }

    @Test
    fun testName() {
        val codec = ShardAddCodec()
        Assert.assertEquals(codec.name(), "ShardAdd")
    }


    @Test
    fun testEncodeDecode() {
        val codec = ShardAddCodec()
        val buffer = Buffer.buffer()
        val value = ShardAdd(HashTriple(10L, 100L, 1000L))

        codec.encodeToWire(buffer, value)
        val newValue = codec.decodeFromWire(0, buffer)

        Assert.assertEquals(value, newValue)
    }

    @Test
    fun testTransform() {
        val codec = ShardAddCodec()
        val value = ShardAdd(HashTriple(10L, 100L, 1000L))
        val newValue = codec.transform(value)

        Assert.assertEquals(value, newValue)
    }
}