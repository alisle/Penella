package org.penella.messages

import org.penella.index.IndexType
import org.penella.structures.triples.HashTriple

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
 * Created by alisle on 1/26/17.
 */
data class ShardGet(val indexType: IndexType, val triple: HashTriple)

class ShardSize() {
    override fun equals(other: Any?): Boolean {
        return other is ShardSize
    }
}

data class ShardSizeResponse(val size: Long)

data class ShardAdd(val triple: HashTriple)