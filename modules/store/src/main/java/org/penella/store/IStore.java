package org.penella.store;

import org.penella.structures.triples.HashTriple;
import org.penella.structures.triples.Triple;

import java.util.Optional;

/**
 * Created by alisle on 6/19/17.
 */
public interface IStore {
    /**
     * Adds a new Value into the Store.
     * @param value Value to add
     * @return Hash Key of the value.
     */
    long add(String value);

    /**
     * Adds all the strings from the Triple into the store.
     * @param triple Triple who's strings need to be added to the store.
     */
    void add(Triple triple);

    /**
     * Gets the string associated with key.
     * @param hash The hash to look up
     * @return The String if it's within the Map
     */
    Optional<String> get(long hash);

    /**
     * Gets the Triple based off the hashes within the Hash Triple
     * @param triple Triple of hashes to look up
     * @return Triple of Strings
     */
    Optional<Triple> get(HashTriple triple);

    /**
     * Returns a hash for the specified value.
     * @param value The value to hash
     * @return The Hash
     */
    long  generateHash(String value);

}
