package org.penella.codecs

import io.vertx.core.buffer.Buffer

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
abstract class JSONCodec {
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
}