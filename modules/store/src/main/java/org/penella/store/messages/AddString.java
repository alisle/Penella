package org.penella.store.messages;

/**
 * Created by alisle on 8/14/17.
 */
final public class AddString {
    private final String value;

    public AddString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
