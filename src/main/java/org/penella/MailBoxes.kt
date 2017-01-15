package org.penella

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
 * Created by alisle on 1/13/17.
 */
enum class MailBoxes(val mailbox : String) {
    NODE_CREATE_DB("node.create.db"),
    NODE_LIST_DBS("node.list.dbs"),

    STORE_ADD_STRING("store.add.string"),
    STORE_ADD_TRIPLE("store.add.triple"),
    STORE_GET_STRING("store.get.string"),
    STORE_GET_HASHTRIPLE("store.get.hashtriple"),
    STORE_GENERATE_HASH("store.generate.hash")
    ;
}