package org.penella.codecs

import org.penella.messages.DatabaseQueryResult

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
 *
 * Created by alisle on 1/27/17.
 */
class DatabaseQueryResultCodec : JSONCodec<DatabaseQueryResult>(DatabaseQueryResult::class.java){
    override fun name()   = "DatabaseQueryResult"
}