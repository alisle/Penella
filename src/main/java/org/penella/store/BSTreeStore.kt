package org.penella.store

import net.openhft.hashing.LongHashFunction
import org.penella.structures.triples.Triple
import org.penella.structures.BSTree

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
 * Created by alisle on 10/17/16.
 */
open class BSTreeStore(seed: Long) : IStore {
    val hashFunction = LongHashFunction.xx_r39(seed)
    val store : BSTree<Long, String> = BSTree()

    override fun generateHash(value: String) : Long {
        return hashFunction.hashChars(value)
    }

    override fun add(value: String) : Long {
        val hash = hashFunction.hashChars(value)
        store.add(hash,value)
        return hash
    }

    override fun add(triple: Triple) {
        store.add(triple.hashSubject, triple.subject)
        store.add(triple.hashProperty, triple.property)
        store.add(triple.hashObj, triple.obj)
    }

    override fun get(value: Long) : String? {
        return store.get(value)
    }
}