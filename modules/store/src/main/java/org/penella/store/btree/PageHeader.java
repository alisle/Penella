package org.penella.store.btree;

import java.util.UUID;

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
 * Created by alisle on 7/24/17.
 */
public class PageHeader {
    /**
     * The UUID of the Page.
     */
    private final UUID uuid;

    /**
     * The maximum size of this page.
     */
    private final int maximum;

    /**
     * If this Page is external.
     */
    private final boolean external;

    /**
     * Current size fo the page.
     */
    private int currentSize;

    /**
     * Creates a Page Header
     * @param uuid UUID of the Header.
     * @param external If this is an external page.
     */
    public PageHeader(UUID uuid, boolean external, int maximum) {
        this.maximum = maximum;
        this.uuid = uuid;
        this.external = external;
    }

    /**
     * Creates a Page Header with a new UUID.
     * @param external If this is an external page.
     */
    public PageHeader(boolean external, int maximum) {
        //TODO: Change this to a non-secure UUID generator.
        this(UUID.randomUUID(), external, maximum);
    }

    /**
     * Get the UUID of the Page
     * @return UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * If this is an external page.
     * @return True if this page is external.
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * Current Size
     * @return
     */
    public int getCurrentSize() {
        return currentSize;
    }

    /**
     * Increate the Size (the number of elements currently within the page
     */
    public void incrementCurrentSize() {
        this.currentSize += 1;
    }

    /**
     * Set the current size  (the number of element currently within the page
     * @param currentSize
     */
    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }


    /**
     * Returns the maximum page size for this page/
     * @return Page Size
     */
    public int getMaxPageSize() {
        return this.maximum;
    }

}
