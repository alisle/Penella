package org.penella.codecs

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.messages.Status
import org.penella.messages.StatusMessage

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
 * Created by alisle on 1/14/17.
 */
class StatusMesageCodecTest {
    @Before
    fun setUp() {
        Json.mapper.registerModule(KotlinModule())
    }

    @Test
    fun testName() {
        val codec = StatusMessageCodec()
        Assert.assertEquals(codec.name(), "StatusMessage")
    }


    @Test
    fun testEncodeDecode() {
        val codec = StatusMessageCodec()
        val buffer = Buffer.buffer()
        val value = StatusMessage(Status.FAILED, "I don't know")

        codec.encodeToWire(buffer, value)
        val codecValue = codec.decodeFromWire(0, buffer)

        Assert.assertEquals(value, codecValue )
    }

    @Test
    fun testTransform() {
        val codec = StatusMessageCodec()
        val value = StatusMessage(Status.FAILED, "I don't know")
        val codecValue = codec.transform(value)

        Assert.assertEquals(value, codecValue)
    }
}