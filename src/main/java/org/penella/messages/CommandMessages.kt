package org.penella.messages

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
 * Created by alisle on 12/12/16.
 */

data class CreateDB(val name : String, val shards : Int)
data class ListDBResponse(val names : Array<String>)
class ListDB {
    override fun equals(other: Any?): Boolean {
        return other is ListDB
    }
}

