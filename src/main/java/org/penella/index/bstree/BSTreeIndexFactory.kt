package org.penella.index.bstree

import org.penella.index.*

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
class BSTreeIndexFactory : IIndexFactory {
    override fun createIndex(index: IndexType): IIndex {
        when(index) {
            IndexType.O -> return ObjectBSTreeIndex()
            IndexType.PO -> return PropertyObjectBSTreeIndex()
            IndexType.SO -> return SubjectObjectBSTreeIndex()
            IndexType.SPO -> return SubjectPropertyObjectBSTreeIndex()
            else -> throw CreateUnsupportedIndex()
        }
    }
}