package org.penella.index

import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.QueryTriple
import org.penella.structures.triples.TripleType


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
 * Created by alisle on 11/4/16.
 */
class InvalidBitMaskException : Exception("Invalid Bitmask!")

enum class IndexType(val ID: Int, val mask: Int) {
    // When I have none it doesn't matter 0b000 which I use.

    // When I have the Subject, Object:
    // 0b000 --> Can be any index
    // 0b001 --> Object Only (Same as OXX SubMap)
    // 0b010 --> Property Only (Same as PXX SubMap)
    // 0b011 --> Property, Object (PO)
    // 0b100 --> Subject Only (Same as SXX)
    // 0b101 --> Subject, Object (S0)
    // 0b110 --> Subject, Property (Same as SPX Submap)
    // 0b111 --> Subject, Property, Object
    O(0, 0b001),
    PO(1, 0b011),
    SO(2, 0b101),
    SPO(3, 0b111),
    ;

    companion object {
        fun getIndexByMask(mask: Int): IndexType {
            when(mask) {
                0b000 -> return SPO
                0b001 -> return O
                0b010 -> return PO
                0b011 -> return PO
                0b100 -> return SPO
                0b101 -> return SO
                0b110 -> return SPO
                0b111 -> return SPO
                else -> throw InvalidBitMaskException()
            }
        }

        fun getIndex(hashTriple : HashTriple) = getIndex(hashTriple.hashSubject, hashTriple.hashProperty, hashTriple.hashObj)

        fun getIndex(subject: Long, property: Long, obj: Long): IndexType {
            var mask = 0b0
            if (subject != 0L) mask += 0b100
            if (property != 0L) mask += 0b010
            if (obj != 0L) mask += 0b001

            return getIndexByMask(mask)
        }

        fun getOrder(index : IndexType) : Triple<TripleType, TripleType, TripleType> {
            when(index) {
                O   -> return Triple(TripleType.OBJECT, TripleType.SUBJECT, TripleType.PROPERTY)
                PO  -> return Triple(TripleType.PROPERTY, TripleType.OBJECT, TripleType.SUBJECT)
                SO  -> return Triple(TripleType.SUBJECT, TripleType.OBJECT, TripleType.PROPERTY)
                SPO -> return Triple(TripleType.SUBJECT, TripleType.PROPERTY, TripleType.OBJECT)
            }
        }
    }

    override fun toString(): String {
        return "IndexType-$ID"
    }


}
