package org.penella.structures.triples

import java.io.Serializable

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
 * Created by alisle on 11/18/16.
 */
open class HashTriple(val hashSubject : Long, val hashProperty : Long, val hashObj : Long) : Serializable {

    companion object {
        val seed : Long = 665445839

        fun create(type : TripleType, values : List<Long>) : Array<HashTriple> {
            return values.map { x ->
                when (type) {
                    TripleType.SUBJECT -> HashTriple(x, 0L, 0L)
                    TripleType.PROPERTY -> HashTriple(0L, x, 0L)
                    TripleType.OBJECT -> HashTriple(0L, 0L, x)
                    TripleType.NONE -> throw InvalidNoneTripleType()
                }
            }.toTypedArray()
        }

        fun create(type: TripleType, value: Long) : Array<HashTriple> {
            return when(type) {
                TripleType.OBJECT -> return arrayOf(HashTriple(hashSubject = 0L, hashProperty = 0L, hashObj = value))
                TripleType.PROPERTY -> return arrayOf(HashTriple(hashSubject = 0L, hashProperty = value, hashObj = 0L))
                TripleType.SUBJECT -> return arrayOf(HashTriple(hashSubject = value, hashProperty = 0L, hashObj = 0L))
                TripleType.NONE -> throw InvalidNoneTripleType()
            }
        }

        fun create(type: TripleType, value: Long, triples : Array<HashTriple>) : Array<HashTriple> {
            return triples.map { x ->
                when(type) {
                    TripleType.OBJECT -> HashTriple(hashSubject = x.hashSubject, hashProperty = x.hashProperty, hashObj = value )
                    TripleType.PROPERTY -> HashTriple(hashSubject = x.hashSubject, hashProperty = value, hashObj = x.hashObj )
                    TripleType.SUBJECT -> HashTriple(hashSubject = value, hashProperty = x.hashProperty, hashObj = x.hashObj )
                    TripleType.NONE -> throw InvalidNoneTripleType()

                }
            }.toTypedArray()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HashTriple) return false

        if (hashSubject != other.hashSubject) return false
        if (hashProperty != other.hashProperty) return false
        if (hashObj != other.hashObj) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hashSubject.hashCode()
        result = 31 * result + hashProperty.hashCode()
        result = 31 * result + hashObj.hashCode()
        return result
    }

    override fun toString(): String {
        return "HashTriple(hashSubject=$hashSubject, hashProperty=$hashProperty, hashObj=$hashObj)"
    }


}
