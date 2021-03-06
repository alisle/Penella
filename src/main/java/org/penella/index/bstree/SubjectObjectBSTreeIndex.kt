package org.penella.index.bstree

import org.penella.index.IndexType
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
 * Created by alisle on 11/29/16.
 */


class SubjectObjectBSTreeIndex() : BSTreeIndex(IndexType.SO) {
    override fun add(triple: HashTriple)  = addTriple(triple.hashSubject, triple.hashObj, triple.hashProperty)
    override fun get(first: TripleType, second: TripleType, firstValue: Long, secondValue: Long) : IndexResultSet = if(first == TripleType.SUBJECT && second == TripleType.OBJECT) getResults(firstValue, secondValue) else throw IncorrectIndexRequest()
    override fun get(first: TripleType, value: Long ) : IndexResultSet = if(first == TripleType.SUBJECT) getResults(value) else throw IncorrectIndexRequest()
}
