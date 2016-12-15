package org.penella.index.bstree

import org.penella.index.IIndex
import org.penella.index.IndexType
import org.penella.query.IncompleteResultSet


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
 * Created by alisle on 11/1/16.
 */

abstract class BSTreeIndex(val index: IndexType) : IIndex {
    protected val rootTree = FirstLayer(index)


    protected fun addTriple(first: Long, second: Long, third: Long) {
        rootTree.add(first, second, third)
    }

    protected fun getResults(value: Long) : IncompleteResultSet {
        return IncompleteResultSet(rootTree.get(value))
    }

    protected fun getResults(first: Long, second: Long) : IncompleteResultSet {
        return IncompleteResultSet(rootTree.get(first, second))
    }

}