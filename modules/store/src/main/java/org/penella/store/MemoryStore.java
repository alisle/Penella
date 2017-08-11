package org.penella.store;

import net.openhft.hashing.LongHashFunction;
import org.penella.structures.btree.BTreeHashMap;
import org.penella.structures.triples.HashTriple;
import org.penella.structures.triples.Triple;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by alisle on 6/20/17.
 */
public class MemoryStore implements IStore {
    private final BTreeHashMap<Long, String> map;
    private final LongHashFunction hashFunction;

    public MemoryStore(int seed, int pageSize) {
        map = new BTreeHashMap<>(pageSize, Long.MIN_VALUE);
        hashFunction = LongHashFunction.xx_r39(seed);
    }

    @Override
    public long add(String value) {
        long key = hashFunction.hashChars(value);
        map.add(key, value);

        return key;
    }

    @Override
    public void add(Triple triple) {
        Arrays.stream(triple.strings()).forEach( x -> map.add(hashFunction.hashChars(x), x));
    }

    @Override
    public Optional<String> get(long key) {
        return map.get(key);
    }

    @Override
    public Optional<Triple> get(HashTriple triple) {
        Optional<String> subject = map.get(triple.getHashSubject());
        Optional<String> property = map.get(triple.getHashProperty());
        Optional<String> obj = map.get(triple.getHashObj());

        if(subject.isPresent() &&
                property.isPresent() &&
                obj.isPresent()) {
            return  Optional.of(new Triple(subject.get(), property.get(), obj.get()));
        }

        return Optional.ofNullable(null);
    }

    @Override
    public long generateHash(String value) {
        return hashFunction.hashChars(value);
    }
}
