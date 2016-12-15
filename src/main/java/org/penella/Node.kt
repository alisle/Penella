package org.penella

import org.penella.database.DatabaseImpl
import org.penella.database.IDatabase
import org.penella.index.IIndexFactory
import org.penella.messages.CreateDB
import org.penella.messages.CreateDBStatus
import org.penella.messages.ListDB
import org.penella.store.IStore
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
 * Created by alisle on 12/12/16.
 */
class Node(private val store: IStore, private val indexFactory: IIndexFactory) {
    val databases = HashMap<String, IDatabase>()

    fun createDB(db : CreateDB) : CreateDBStatus {
        if( databases.containsKey(db.name)) {
            return CreateDBStatus(db.name, Status.FAILED, "Database with that name already exists!")
        }

        databases.put(db.name, DatabaseImpl(db.name, store, indexFactory, db.shards))

        return CreateDBStatus(db.name, Status.SUCESSFUL)
    }

    fun listDB() : ListDB {
        return ListDB(databases.keys.toTypedArray())
    }



}