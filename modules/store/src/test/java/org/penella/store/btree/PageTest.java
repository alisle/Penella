package org.penella.store.btree;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;
import org.penella.store.btree.Page;
import org.penella.store.btree.PersistenceHandler;

import java.io.File;
import java.util.Iterator;

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
 * Created by alisle on 6/9/17.
 */
public class PageTest {
    private PersistenceHandler createHandler() {
        final File tempDir = Files.createTempDir();
        tempDir.deleteOnExit();

        String directory = tempDir.getAbsolutePath();
        PersistenceHandler handler = new PersistenceHandler(directory, 1024, 10, 100, 2);

        return handler;
    }

    @Test
    public void testAddKeyInternalNode() throws Exception {
        PersistenceHandler handler = createHandler();
        Page page = new Page( handler,10, false);
        try {
            page.add(1, null);
        } catch(RuntimeException ex) {
            return;
        }

        Assert.assertFalse("Shouldn't be able to add a key to an internal node", true);
    }

    @Test
    public void testAddPageExternalNode() {
        PersistenceHandler handler = createHandler();

        Page page = new Page(handler, 10, true);
        try {
            page.add(new Page(handler, 10, true));
        } catch(RuntimeException ex) {
            return;
        }

        Assert.assertFalse("Shouldn't be able to add a page to an external node", true);
    }

    @Test
    public void testAddKey() throws Exception {
        PersistenceHandler handler = createHandler();
        Page page = new Page(handler, 10, true);

        page.add(1, "1");
        page.add(4, "4");
        page.add(3, "3");
        page.add(0, "0");
        page.add(9, "9");
        page.add(2, "2");
        page.add(6, "6");
        page.add(5, "5");
        page.add(7, "7");
        page.add(8, "8");

        Iterator iterator = page.keys().iterator();

        for(int num = 0; num < 10; num++) {
            long next = (Long)iterator.next();
            Assert.assertEquals("They should of come out in order", next, num);
        }

    }

    @Test
    public void testFull() throws Exception{
        PersistenceHandler handler = createHandler();

        Page page = new Page(handler,3, true);
        page.add(0,  "Hello");
        page.add(1, "Every");
        page.add(2, "Body");

        Assert.assertTrue(page.isFull());

        try {
            page.add(3, null);
        } catch(RuntimeException ex) {
            return;
        }

        Assert.assertFalse("Shouldn't be able to add more to a full page", true);
    }

    @Test
    public void testContains() throws Exception {
        PersistenceHandler handler = createHandler();

        Page page = new Page(handler,5, true);
        for(int x = 0; x < 5; x++ ){
            page.add(x, x + "");
            Assert.assertTrue(page.contains(x));
        }

        Assert.assertFalse(page.contains(99));

    }

    @Test
    public void testExternalNext() {
        PersistenceHandler handler = createHandler();

        Page page = new Page(handler, 5, true);
        try {
            page.next(1);
        } catch (RuntimeException ex) {
            return;
        }

        Assert.assertFalse("Shouldn't be able get next from External page", true);
    }

    @Test
    public void testNext() throws Exception {
        PersistenceHandler handler = createHandler();

        Page page = new Page(handler, 5, false);

        Page children[] = new Page[5];
        for(int x = 0; x < 10; x++) {
            if(x % 2 == 1) {
                children[x / 2] = new Page(handler, 5, true);
                children[x / 2].add(x, "" + x);
            }
        }

        page.add(children[1]);
        page.add(children[0]);
        page.add(children[3]);
        page.add(children[2]);
        page.add(children[4]);

        Assert.assertEquals(page.next(2), children[0]);
        Assert.assertEquals(page.next(3), children[1]);

        Assert.assertEquals(page.next(100), children[4]);
    }

    @Test
    public void testSplitExternal() throws Exception {
        PersistenceHandler handler = createHandler();

        Page page = new Page(handler,4, true);
        for(int x = 0; x < 4; x++) {
            page.add(x, x + "");
        }

        Page top = page.split();

        Assert.assertEquals(2, top.size());
        Assert.assertEquals( 2, page.size());

        Iterator iterator = page.keys().iterator();
        Assert.assertEquals(0L, iterator.next());
        Assert.assertEquals(1L, iterator.next());

        Assert.assertFalse(iterator.hasNext());

        iterator = top.keys().iterator();
        Assert.assertEquals(2L, iterator.next());
        Assert.assertEquals(3L, iterator.next());

        Assert.assertFalse(iterator.hasNext());
    }


}
