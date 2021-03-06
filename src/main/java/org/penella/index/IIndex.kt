package org.penella.index

import org.penella.messages.IndexResultSet
import org.penella.structures.triples.HashTriple
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
 * Created by alisle on 9/27/16.
 */

interface IIndex {
    fun add(triple: HashTriple)
    fun get(first: TripleType, value: Long) : IndexResultSet
    fun get(first: TripleType, second: TripleType, firstValue: Long, secondValue: Long) : IndexResultSet
}
