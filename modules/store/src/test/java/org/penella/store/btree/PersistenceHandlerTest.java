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
                4,
                5,
                1024,
                2);
        UUID uuid = UUID.randomUUID();
        int position = handler.put(uuid, "Hello World");
        String world = handler.get(uuid, position);

        Assert.assertEquals("Hello World", world);
    }

    @Test
    public void TestPutGetCache() throws Exception {
        PersistenceHandler handler = new PersistenceHandler(tempDir(),
                4,
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

    @Test
    public void TestPutGetPutGet() throws Exception {
        PersistenceHandler handler = new PersistenceHandler(tempDir(),
                200,
                100,
                100000,
                2);

        UUID uuid = UUID.randomUUID();
        int position[] = new int[10];
        int newposition[] = new int[10];

        for(int x = 0; x < 10; x++) {
            position[x] = handler.put(uuid, "" + x);
        }

        for(int x = 9; x >= 0; x--) {
            Assert.assertEquals("" + x, handler.get(uuid, position[x]));
        }

        for(int x = 0; x < 10; x++){
            newposition[x] = handler.put(uuid, "New " + x);
            Assert.assertEquals("New " + x, handler.get(uuid, newposition[x]));
        }

        for(int x = 9; x >= 0; x--) {
            Assert.assertEquals("" + x, handler.get(uuid, position[x]));
        }

    }

    @Test
    public void TestPutGetLarge() throws Exception {
        PersistenceHandler handler = new PersistenceHandler(tempDir(),
                200,
                100,
                100000,
                2);

        int COUNT = 100000;
        int position[] = new int[COUNT];
        UUID uuid = UUID.randomUUID();

        for(int x = 0; x < COUNT; x++) {
            position[x] = handler.put(uuid, "" + x);
        }

        for(int x = 0; x < COUNT; x++) {
            String value = handler.get(uuid, position[x]);
            Assert.assertEquals("" + x, value);
        }

    }
}

