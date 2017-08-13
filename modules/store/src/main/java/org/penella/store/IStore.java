package org.penella.store;

import org.penella.structures.triples.HashTriple;
import org.penella.structures.triples.Triple;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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
 * Created by alisle on 6/19/17.
 */
public interface IStore {
    /**
     * Adds a new Value into the Store.
     * @param value Value to add
     * @return Hash Key of the value.
     */
    long add(String value) throws IOException, ExecutionException;

    /**
     * Adds all the strings from the Triple into the store.
     * @param triple Triple who's strings need to be added to the store.
     */
    void add(Triple triple) throws IOException, ExecutionException;

    /**
     * Gets the string associated with key.
     * @param hash The hash to look up
     * @return The String if it's within the Map
     */
    Optional<String> get(long hash) throws IOException, ExecutionException;

    /**
     * Gets the Triple based off the hashes within the Hash Triple
     * @param triple Triple of hashes to look up
     * @return Triple of Strings
     */
    Optional<Triple> get(HashTriple triple) throws IOException, ExecutionException;

    /**
     * Returns a hash for the specified value.
     * @param value The value to hash
     * @return The Hash
     */
    long  generateHash(String value);

}
