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
