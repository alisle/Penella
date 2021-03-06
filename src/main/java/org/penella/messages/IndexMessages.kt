package org.penella.messages

import org.penella.query.IResultSet
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.TripleType
import org.penella.structures.triples.Triple

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
 * Created by alisle on 12/4/16.
 */

data class IndexAdd(val triple: HashTriple)
data class IndexGetFirstLayer(val type: TripleType, val first: Long)
data class IndexGetSecondLayer(val firstType: TripleType, val firstValue: Long, val secondType: TripleType, val secondValue: Long)
data class IndexResultSet(val triples: Array<HashTriple>) : IResultSet