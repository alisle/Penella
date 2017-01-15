package org.penella.messages

import org.penella.structures.triples.HashTriple
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
 * Created by alisle on 1/14/17.
 */
data class StoreAddString(val value : String)
data class StoreAddStringResponse(val value: Long)

data class StoreAddTriple(val value: Triple)

data class StoreGetString(val value: Long)
data class StoreGetStringResponse(val value: String?)

data class StoreGetHashTriple(val value: HashTriple)
data class StoreGetHashTripleResponse(val value: Triple?)

data class StoreGenerateHash(val value: String)
data class StoreGenerateHashResponse(val value: Long?)