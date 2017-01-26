package org.penella.codecs

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.Json
import org.slf4j.LoggerFactory

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
abstract class JSONCodec<T>(val clazz : Class<*> ) : MessageCodec<T, T> {
    protected fun extractJSON(pos: Int, buffer: Buffer) : String {
        val size = buffer!!.getInt(pos)
        val bytesStart = pos + 4
        val bytes = buffer.getBytes(bytesStart, bytesStart + size)
        return bytes.toString(Charsets.UTF_8)
    }

    protected fun insertJSON(buffer: Buffer, json : String)  {
        val bytes = json.toByteArray(Charsets.UTF_8)
        val size = bytes.size

        buffer!!.appendInt(size)
        buffer.appendBytes(bytes)

    }

    override fun transform(s: T?): T {
        return s!!
    }

    override fun systemCodecID(): Byte {
        return -1
    }


    override fun decodeFromWire(pos: Int, buffer: Buffer?): T {
        val json = extractJSON(pos, buffer!!)

        return Json.decodeValue(json, clazz) as T
    }

    override fun encodeToWire(buffer: Buffer?, s: T) {
        insertJSON(buffer!!, Json.encode(s))
    }

    abstract override fun name(): String
}
