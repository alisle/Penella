package org.penella.store.btree;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;
import org.penella.store.btree.PersistenceHandler;

import java.io.File;
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
 * Created by alisle on 8/9/17.
 */
public class PersistenceHandlerTest {
    private String tempDir() {
        final File tempDir = Files.createTempDir();
        tempDir.deleteOnExit();

        return tempDir.getAbsolutePath();
    }

    @Test
    public void TestPutGet() throws Exception {
        PersistenceHandler handler = new PersistenceHandler(tempDir(),
                200,
                5,
                100,
                2);
        UUID uuid = UUID.randomUUID();
        int position = handler.put(uuid, "Hello World");
        String world = handler.get(uuid, position);

        Assert.assertEquals("Hello World", world);
    }

    @Test
    public void TestPutGetCache() throws Exception {
        PersistenceHandler handler = new PersistenceHandler(tempDir(),
                200,
                1,
                100,
                2);

        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        int position = handler.put(uuid, "Hello World");
        int position2 = handler.put(uuid2, "Hello World 2");

        String world = handler.get(uuid, position);
        String world2 = handler.get(uuid2, position2);

        Assert.assertEquals("Hello World", world);
        Assert.assertEquals("Hello World 2", world2);

    }
}

