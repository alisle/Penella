package org.penella.structures.triples

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.vertx.core.json.Json
import net.openhft.hashing.LongHashFunction

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
 * Created by alisle on 9/30/16.
 */

open class Triple(val subject: String, val property: String, @JsonProperty("object") val obj: String)  {

    @JsonIgnore val hashTriple = HashTriple(LongHashFunction.xx_r39(HashTriple.seed).hashChars(subject),
            LongHashFunction.xx_r39(HashTriple.seed).hashChars(property),
            LongHashFunction.xx_r39(HashTriple.seed).hashChars(obj))

    companion object {
        fun fromJSON(json: String) = Json.decodeValue(json, Triple::class.java)
        fun getHash(value: String) = LongHashFunction.xx_r39(HashTriple.seed).hashChars(value)
    }

    @JsonIgnore
    val hash = LongHashFunction.xx_r39(HashTriple.seed).hashChars(this.toString())


    fun toJSON() = Json.encode(this)

    override fun toString(): String {
        return "'$subject'-'$property'-'$obj'."
    }

    override fun equals(other: Any?): Boolean {
        if (other is Triple && other.hash == this.hash) {
            if (other.hashTriple.hashSubject == this.hashTriple.hashSubject &&
                    other.hashTriple.hashProperty == this.hashTriple.hashProperty &&
                    other.hashTriple.hashObj == this.hashTriple.hashObj) {
                return true
            }
        }

        return false
    }




}