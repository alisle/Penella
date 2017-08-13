package org.penella.store.btree;

import org.penella.store.IStore;
import org.penella.structures.triples.HashTriple;
import org.penella.structures.triples.Triple;

import java.io.IOException;
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
 * Created by alisle on 8/11/17.
 */
public class BTreeStore implements IStore {
    protected final int maximumSize;
    protected final PersistenceHandler handler;

    protected Page root;

    public BTreeStore(int maximumSize, PersistenceHandler handler) throws ExecutionException, IOException {
        this.maximumSize = maximumSize;
        this.handler = handler;

        root = new Page(handler, maximumSize, true);
        root.add(Long.MIN_VALUE, "Sentinal");
    }

    /**
     * Adds a new Value into the Store.
     * @param value Value to add
     * @return Hash Key of the value.
     */
    @Override
    public long add(String value) throws ExecutionException, IOException {
        long hash = generateHash(value);

        add(root, hash, value);
        if(root.isFull()) {
            Page leftHalf = root;
            Page rightHalf = root.split();
            root = new Page(handler, maximumSize, false);
            root.add(leftHalf);
            root.add(rightHalf);
        }

        return hash;
    }

    /**
     * Adds all the strings from the Triple into the store.
     * @param triple Triple who's strings need to be added to the store.
     */
    @Override
    public void add(Triple triple) throws ExecutionException, IOException {
        add(triple.getSubject());
        add(triple.getProperty());
        add(triple.getObj());
    }

    /**
     * Helper function we handles the pages if they're full.
     * @param page Page we're looking add
     * @param key The Hash
     * @param value the value we're inserting.
     * @throws ExecutionException
     * @throws IOException
     */
    private void add(Page page, long key, String value) throws ExecutionException, IOException {
        if(page.isExternal()) {
            page.add(key, value);
        } else {
            Page next = page.next(key);
            add(next, key, value);
            if(next.isFull()) {
                page.add(next.split());
            }
        }
    }

    /**
     * Gets the string associated with key.
     * @param hash The hash to look up
     * @return The String if it's within the Map
     */
    @Override
    public Optional<String> get(long hash) throws ExecutionException, IOException {
        return get(root, hash);
    }

    /**
     * Internal Helper to recursively get value
     * @param page Page to look at
     * @param hash The hash to lookup
     * @return Optional of the value.
     * @throws ExecutionException
     * @throws IOException
     */
    private Optional<String> get(Page page, long hash) throws ExecutionException, IOException {
        return (page.isExternal()) ? page.get(hash) : get(page.next(hash), hash);
    }


    /**`
     * Gets the Triple based off the hashes within the Hash Triple
     * @param triple Triple of hashes to look up
     * @return Triple of Strings
     */
    @Override
    public Optional<Triple> get(HashTriple triple) throws ExecutionException, IOException {
        Optional<String> subject = get(triple.getHashSubject());
        Optional<String> property = get(triple.getHashProperty());
        Optional<String> obj = get(triple.getHashObj());

        if(!subject.isPresent() || !property.isPresent() || !obj.isPresent()) {
            return Optional.ofNullable(null);
        }

        return Optional.of(new Triple(subject.get(), property.get(), obj.get()));
    }

    @Override
    public long generateHash(String value) {
        return Triple.Companion.getHash(value);
    }
}
