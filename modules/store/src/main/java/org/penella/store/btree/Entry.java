package org.penella.store.btree;

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
