package org.penella.structures.triples

import org.penella.index.IndexType

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
 * Created by alisle on 12/30/16.
 */


class QueryTriple(val subject : String, val property :  String, val obj : String)  {

    val variables : Array<String>
    val hashTriple : HashTriple
    val indexType : IndexType

    constructor(triple : Triple) : this(triple.subject, triple.property, triple.obj)

    init {
        variables = arrayOf(subject, property, obj).filter{ x -> x.startsWith("?")}.toTypedArray()
        hashTriple = HashTriple(
                if(subject.startsWith("?")) 0L else Triple.getHash(subject),
                if(property.startsWith("?")) 0L else Triple.getHash(property),
                if(obj.startsWith("?")) 0L else Triple.getHash(obj)
        )

        indexType = IndexType.Companion.getIndex(hashTriple)

    }

    fun getType(variable : String) : TripleType {
        if(variable.startsWith("?")) {
            when(variable) {
                subject -> return TripleType.SUBJECT
                property -> return TripleType.PROPERTY
                obj -> return TripleType.OBJECT
                else -> return TripleType.NONE
            }
        }

        return TripleType.NONE
    }


}