package org.penella.store.messages;

/**
 * Created by alisle on 8/14/17.
 */
public class AddStringResponse {
    private final long hash;
    private final String value;

    public AddStringResponse(long hash, String value) {
        this.hash = hash;
        this.value = value;
    }

    public long getHash() {
        return hash;
    }

    public String getValue() {
        return value;
    }
}
