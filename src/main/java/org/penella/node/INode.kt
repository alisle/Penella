package org.penella.node

import org.penella.messages.CreateDB
import org.penella.messages.ListDB
import org.penella.messages.ListDBResponse
import org.penella.messages.StatusMessage

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
interface INode {
    fun createDB(db : CreateDB) : StatusMessage
    fun listDB(list : ListDB) : ListDBResponse
}