package org.penella.store.btree;

/**
 * Created by alisle on 7/29/17.
 */
public final class ExternalEntry extends Entry {
    /**
     * Position in the file to load the value from.
     */
    private final int position;


    /**
     * Constructs an external Entry
     * @param key Key to use
     * @param position position within the file to find the string.
     */
    public ExternalEntry(PageHeader parent, long key, int position) {
        super(true, parent, key);
        this.position = position;
    }

    /**
     * Retursn the Value
     * @return Position
     */
    public int getPosition() {
        return position;
    }
}

