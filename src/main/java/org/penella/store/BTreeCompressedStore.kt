package org.penella.store

import net.openhft.hashing.LongHashFunction
import org.penella.smaz.Smaz
import org.penella.structures.BSTree
import org.penella.structures.triples.Triple
import org.slf4j.Logger
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
 * Created by alisle on 10/27/16.
 */
class BTreeCompressedStore(seed: Long, val maxStringSize : Int) : IStore {
    val hashFunction = LongHashFunction.xx_r39(seed)
    val compressor = Smaz()
    val store : BSTree<Long, ByteArray> = BSTree()

    companion object {
        val log : Logger = LoggerFactory.getLogger(BTreeCompressedStore::class.java)
    }

    override fun generateHash(value: String) : Long {
        return hashFunction.hashChars(value)
    }

    override fun add(value: String) : Long {
        //if(log.isTraceEnabled) log.trace("Adding Value:" + value)
        val hash = hashFunction.hashChars(value)
        store.add(hash, compressor.compressString(value))
        return hash
    }

    override fun add(triple: Triple) {
        //if(log.isTraceEnabled) log.trace("Adding Triple:" + triple)

        store.add(triple.hashSubject, compressor.compressString(triple.subject))
        store.add(triple.hashProperty, compressor.compressString(triple.property))
        store.add(triple.hashObj, compressor.compressString(triple.obj))
    }

    override fun get(value: Long) : String? {
        val buffer = store.get(value)
        when(buffer) {
            null -> return null
            else -> return compressor.decompressString(buffer)
        }
    }
}