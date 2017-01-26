package org.penella.codecs

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.messages.ShardSize
import org.penella.messages.ShardSizeResponse

/**
 * Created by alisle on 1/26/17.
 */
class ShardSizeCodecTest {
    @Before
    fun setUp() {
        Json.mapper.registerModule(KotlinModule())
    }

    @Test
    fun testName() {
        val codec = ShardSizeCodec()
        Assert.assertEquals(codec.name(), "ShardSize")
    }


    @Test
    fun testEncodeDecode() {
        val codec = ShardSizeCodec()
        val buffer = Buffer.buffer()
        val value = ShardSize()

        codec.encodeToWire(buffer, value)
        val newValue = codec.decodeFromWire(0, buffer)

        Assert.assertEquals(value, newValue)
    }

    @Test
    fun testTransform() {
        val codec = ShardSizeCodec()
        val value = ShardSize()
        val newValue = codec.transform(value)

        Assert.assertEquals(value, newValue)
    }
}