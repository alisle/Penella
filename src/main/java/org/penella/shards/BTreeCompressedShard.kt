package org.penella.shards

import org.penella.Messages.Exceptions
import org.penella.structures.BSTree
import org.penella.Triple

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicLong

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
 * Created by alisle on 10/28/16.
 */

class InvalidBitMaskException : Exception(Exceptions.BTREE_INVALID_MASK.description)

class BTreeCompressedShard : IShard {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(BTreeCompressedShard::class.java)
    }

    enum class TreeName(val ID: Int, val mask: Int) {
        // When I have none it doesn't matter 0b000 which I use, it's the whole tree

        // When I have the Subject, Object:
        // 0b000 --> Can be tree
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
            fun getTreeByMask(mask: Int): TreeName {
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

            fun getTree(subject: String?, property: String, obj: String?): TreeName {
                var mask = 0b0
                if (!subject.isNullOrEmpty()) mask += 0b100
                if (!property.isNullOrEmpty()) mask += 0b010
                if (!obj.isNullOrEmpty()) mask += 0b001

                return getTreeByMask(mask)
            }
        }
    }

    val trees = Array(TreeName.values().size, { i -> BSTree<Long>() })
    val counter = AtomicLong(0L)
    override  fun size() = counter.get()
    override fun put(value: Triple) {
        if(log.isDebugEnabled) {
            log.debug("Inserting Triple:" + value)
        }

        counter.incrementAndGet()
        trees[TreeName.O.ID].add()
    }

}