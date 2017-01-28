package org.penella.codecs

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.messages.DatabaseBulkAddTriples
import org.penella.messages.DatabaseQuery
import org.penella.query.Query
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
 *
 * Created by alisle on 1/27/17.
 */
class DatabaseQueryCodecTest {
    @Before
    fun setUp() {
        Json.mapper.registerModule(KotlinModule())
    }

    @Test
    fun testName() {
        val codec = DatabaseQueryCodec()
        Assert.assertEquals(codec.name(), "DatabaseQuery")
    }


    @Test
    fun testEncodeDecode() {
        val codec = DatabaseQueryCodec()
        val buffer = Buffer.buffer()
        val value = DatabaseQuery(Query(arrayOf("Hello", "World"), arrayOf("I am a variable", "So am I"), arrayOf(Triple("S0", "P0", "O0"), Triple("S1", "P1", "O1"), Triple("S2", "P2", "O2"))))

        codec.encodeToWire(buffer, value)
        val codecValue = codec.decodeFromWire(0, buffer)

        Assert.assertEquals(value, codecValue )
    }

    @Test
    fun testTransform() {
        val codec = DatabaseQueryCodec()
        val value = DatabaseQuery(Query(arrayOf("Hello", "World"), arrayOf("I am a variable", "So am I"), arrayOf(Triple("S0", "P0", "O0"), Triple("S1", "P1", "O1"), Triple("S2", "P2", "O2"))))
        val codecValue = codec.transform(value)

        Assert.assertEquals(value, codecValue)
    }
}