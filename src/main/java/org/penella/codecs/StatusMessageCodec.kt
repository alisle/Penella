package org.penella.codecs

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.Json
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
class StatusMessageCodec : MessageCodec<StatusMessage, StatusMessage>, JSONCodec() {
    override fun name(): String {
        return "StatusMessage"
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer?): StatusMessage {
        val json = extractJSON(pos, buffer!!)
        return Json.decodeValue(json, StatusMessage::class.java)
    }

    override fun encodeToWire(buffer: Buffer?, s: StatusMessage?) {
        insertJSON(buffer!!, Json.encode(s))
    }

    override fun transform(s: StatusMessage?): StatusMessage {
        return s!!
    }

    override fun systemCodecID(): Byte {
        return -1
    }
}