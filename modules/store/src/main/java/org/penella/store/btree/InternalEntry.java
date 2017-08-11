package org.penella.store.btree;

/**
 * Created by alisle on 7/29/17.
 */
public class InternalEntry extends Entry {
    /**
     * The next page this entry holds.
     */
    private final Page next;

    /**
     * Creates a new internal entry
     * @param key Key to use
     * @param next The page it points to.
     */
    public InternalEntry(PageHeader parent, long key, Page next) {
        super(false, parent, key);
        this.next = next;
    }

    /**
     * Gets the next page.
     * @return The next page.
     */
    public Page getNext() {
        return next;
    }
}
