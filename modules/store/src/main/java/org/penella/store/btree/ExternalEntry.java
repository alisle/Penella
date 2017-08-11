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

