package org.penella.codecs

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.Json
import org.penella.messages.StoreAddString

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
 * Created by alisle on 1/15/17.
 */
class StoreAddStringCodec : MessageCodec<StoreAddString, StoreAddString>, JSONCodec() {
    override fun systemCodecID(): Byte {
        return -1;
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer?): StoreAddString {
        val json = extractJSON(pos, buffer!!)
        return Json.decodeValue(json, StoreAddString::class.java)
    }

    override fun encodeToWire(buffer: Buffer?, s: StoreAddString?) {
        insertJSON(buffer!!, Json.encode(s))
    }

    override fun name(): String {
        return "StoreAddString"
    }

    override fun transform(s: StoreAddString?): StoreAddString {
        return s!!
    }
}