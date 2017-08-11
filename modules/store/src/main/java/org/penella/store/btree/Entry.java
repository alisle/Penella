package org.penella.store.btree;

/**
 * Created by alisle on 7/29/17.
 */
public abstract class Entry {

    /**
     * The Page Header this entry belongs to.
     */
    private final PageHeader header;

    /**
     * if the Entry is External.
     */
    private final boolean isExternal;


    /**
     * The key for this Entry
     */
    private final long key;

    /**
     * If this Entry is External
     * @param isExternal
     */
    public Entry(boolean isExternal, PageHeader header, long key) {
        this.header = header;
        this.isExternal = isExternal;
        this.key = key;
    }

    /**
     * Returns the Key for this entry.
     * @return The Key
     */
    public long getKey() {
        return key;
    }

    /**
     * Returns the Page Header this entry is associated with.
     * @return The Page Header
     */
    public PageHeader getHeader() {
        return header;
    }

    /**
     * Returns if this entry is external.
     * @return True if external
     */
    public boolean isExternal() {
        return isExternal;
    }

}
