package org.penella.store.btree;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
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
 * Created by alisle on 7/29/17.
 */
public class Page {
    private static Logger log = LoggerFactory.getLogger(Page.class);

    /**
     * Internal Class used to get Results
     *
     */
    private final static class SearchResults {
        private final int position;
        private final boolean found;
        private final int filePosition;

        /**
         * Constructs the Search Results
         * @param found True if the key is within this Page
         * @param position Position within the Page if found, otherwise the place to insert a page.
         * @param filePosition If found the value found at that key.
         */
        public SearchResults(boolean found, int position, int filePosition) {
            this.position = position;
            this.found = found;
            this.filePosition = filePosition;
        }

        @Override
        public String toString() {
            return "SearchResults{" +
                    "position=" + position +
                    ", found=" + found +
                    ", file position=" + filePosition +
                    '}';
        }
    }


    /**
     * The Page Header which defines this page.
     */
    private final PageHeader header;

    /**
     * The Handler for storing of the data
     */
    //TODO: Abstract this out.
    private final PersistenceHandler handler;

    /**
     * The Entries.
     */
    private Entry[] entries;


    public boolean isExternal() { return header.isExternal(); }

    public Page(@NotNull  PersistenceHandler handler, int maximum, boolean isExternal) {
        this.handler = handler;
        this.header = new PageHeader(isExternal, maximum);
        if(isExternal) {
            this.entries = new ExternalEntry[maximum + 1];
        } else {
            this.entries = new InternalEntry[maximum +  1];
        }


    }

    /**
     * Gets the value associated with the Key
     * @param key The key used for look up
     * @return The value stored at the key.
     */
    // TODO: This should be an optional
    public Optional<String> get(long key) throws IOException, ExecutionException {
        SearchResults results = position(key);
        return Optional.ofNullable(handler.get(header.getUuid(), results.filePosition));
    }

    /**
     * Checks to see if this page contains that key.
     * @return True if this page contains the key.
     */
    public boolean contains(long key) {
        return position(key).found;
    }

    /**
     * Finds the position within the Array the key should be placed.
     * @param key Key to compare again
     * @return Position within the Array
     */
    private SearchResults position(long key) {
        int x;
        //TODO: Change this to do a binary search.
        loop: for(x = 0; x < header.getCurrentSize(); x++) {
            if(entries == null || entries[x] == null) {
                log.error("We are empty");
                return new SearchResults(false, -1, -1);
            }

            if(key == entries[x].getKey()) {
                if(log.isTraceEnabled()) { log.trace("Found Key: " + key); }
                return new SearchResults(true, x, (entries[x] instanceof ExternalEntry) ? ((ExternalEntry)entries[x]).getPosition() : -1);

            } else if(key < entries[x].getKey()) {
                break loop;
            }
        }

        return new SearchResults(false, x, -1);
    }

    /**
     * Returns the Page that could contain the key
     * @param key Key that is being looked for
     * @return Page which could contain the key.
     */
    public Page next(long key) {
        if(header.isExternal()) {
            throw new RuntimeException("Unable to get a page at an external node");
        }

        SearchResults results = position(key);

        return results.found ?
                ((InternalEntry)entries[results.position]).getNext() :
                ((InternalEntry)entries[results.position - 1]).getNext();
    }

    /**
     * Checks to see if the page is full.
     * @return True if the page is full.
     */
    public boolean isFull() {
        return header.getCurrentSize() == header.getMaxPageSize();
    }


    /**
     * States the current indexSize of the page
     * @return The current page indexSize
     */
    public int size() {
        return header.getCurrentSize();
    }

    /**
     * Adds the child page to this page
     * @param page Page to add as a child
     */
    public void add(Page page) {
        if(header.isExternal()) {
            throw new RuntimeException("Unable to insert page at external level!");
        }

        if(isFull()) {
            throw new RuntimeException("This node is already full and needs to be split");
        }


        long smallest = (long)page.keys().iterator().next();
        int position = position(smallest).position;
        if(log.isTraceEnabled()) { log.trace(getHeader().getUuid() + " : Adding new page: " + page.getHeader().getUuid() + ", position: "  + position  ); }

        for(int x = header.getCurrentSize() + 1; x  > position; x--) {
            entries[x] = entries[x - 1];
        }

        entries[position] = new InternalEntry(header, smallest, page);
        header.incrementCurrentSize();
    }

    /**
     * Adds a key to the page
     * @param key Key to add to the page
     */
    public void add(long key, String value) throws ExecutionException, IOException {
        if(!header.isExternal()) {
            throw new RuntimeException("Unable to insert key at internal level!");
        }

        if(isFull()) {
            throw new RuntimeException("This node is already full and needs to be split");
        }

        int position = position(key).position;
        for(int x = header.getCurrentSize() + 1; x  > position; x--) {
            entries[x] = entries[x - 1];
        }
        if(log.isTraceEnabled()) { log.trace(getHeader().getUuid() + " : Adding new entry: K: " + key + ", V: " + value + ", position: "  + position  ); }

        int filePosition = handler.put(header.getUuid(), value);
        entries[position] = new ExternalEntry(header, key,  filePosition);
        header.incrementCurrentSize();

    }

    /**
     * Splits this page returning a new page with the higher values entries.
     * @return Page containing the higher values.
     */
    public Page split() throws ExecutionException, IOException {
        if(log.isTraceEnabled()) { log.trace("Splitting Page:" + header.getUuid()); }

        Page page = new Page(handler, header.getMaxPageSize(), this.isExternal());
        int pivot = header.getCurrentSize() / 2;
        for(int x = pivot; x < header.getCurrentSize(); x++) {
            if(isExternal()) {
                final ExternalEntry entry = (ExternalEntry)entries[x];
                if(log.isTraceEnabled()) { log.trace("Splitting External Entry: " + header.getUuid() + " with hash: " + entry.getKey() + ", position: " + entry.getPosition()); }
                page.add(entry.getKey(), handler.get(header.getUuid(), entry.getPosition()));
            } else {
                final InternalEntry entry = (InternalEntry)entries[x];
                if(log.isTraceEnabled()) { log.trace("Splitting Internal Entry: " + header.getUuid() + " with hash: " + entry.getKey() + ", next: " + entry.getHeader().getUuid()); }
                page.add(entry.getNext());
            }

            this.entries[x] = null;
        }

        header.setCurrentSize(header.getCurrentSize()/ 2);

        return page;
    }

    /**
     * Returns an interator over the keys
     * @return
     */
    public Iterable<Long> keys() {
        return () -> new Iterator<Long>() {
            int next = 0;

            @Override
            public boolean hasNext() {
                return next < header.getCurrentSize();
            }

            @Override
            public Long next() {
                long returnValue = entries[next].getKey();
                next = next + 1  ;
                return returnValue;
            }
        };
    }

    /**
     * Returns an iterator over the entries
     * @return
     */
    public Iterable<Entry> entries() {
        return () -> new Iterator<Entry>() {
            int next = 0;
            @Override
            public boolean hasNext() {
                return next < header.getCurrentSize();
            }

            @Override
            public Entry next() {
                final Entry entry = entries[next];
                next += 1;

                return entry;
            }
        };
    }

    /**
     * Gets the Page Header
     * @return The Header
     */
    public PageHeader getHeader() {
        return header;
    }


    @Override
    public String toString() {
        return "Page{" +
                "external=" + header.isExternal() +
                ", currentSize=" + header.getCurrentSize() +
                ", entries=" + Arrays.toString(entries) +
                '}';
    }
}
