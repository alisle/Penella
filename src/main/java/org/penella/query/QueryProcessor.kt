package org.penella.query

import org.penella.database.IDatabase
import org.penella.messages.RawQuery
import org.penella.store.IStore
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.Triple
import org.penella.structures.triples.QueryTriple
import java.util.*

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
 * Created by alisle on 12/23/16.
 */

class QueryProcessor(val database: IDatabase, val store : IStore) {

    fun process(raw: RawQuery) : Array<Triple?> {
        val outputs = raw.outputs
        val queries = raw.triples.map { QueryTriple(it) }.toTypedArray()
        val potentials = HashMap<String, Set<HashTriple>>()

        for(query in queries) {
            val triples = database.get(query.indexType, query.hashTriple)
            for(variable in query.variables) {
                if(potentials.containsKey(variable)) {
                    val exisitingTriples = potentials[variable]
                    val union = exisitingTriples!!.union(triples)
                    potentials.put(variable, union)
                } else {
                    potentials.put(variable, triples)
                }
            }
        }


        var result : Set<HashTriple>? = null

        for(output in outputs) {
            if(potentials.containsKey(output)) {
                if(result == null) {
                    result = potentials[output]
                } else {
                    val  potential : Set<HashTriple> = potentials[output]!!
                    result = result.union(potential)
                }
            }
        }


        return result!!.map{ store.get(it) }.filter { it != null }.toTypedArray()
    }
}