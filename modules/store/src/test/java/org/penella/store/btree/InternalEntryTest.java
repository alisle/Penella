package org.penella.store.btree;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;


import java.io.File;

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
 * Created by alisle on 8/10/17.
 */
public class InternalEntryTest {

    @Test
    public void testGetters() {
        final File tempDir = Files.createTempDir();
        tempDir.deleteOnExit();

        String directory = tempDir.getAbsolutePath();
        PersistenceHandler handler = new PersistenceHandler(directory, 4, 10, 1024, 2);

        Page page = new Page(handler, 10, false);
        PageHeader header = page.getHeader();
        InternalEntry entry = new InternalEntry(header, 1001, page);

        Assert.assertEquals(1001, entry.getKey());
        Assert.assertEquals(page, entry.getNext());
        Assert.assertEquals(header, entry.getHeader());
    }
}
