package org.penella.store.btree;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

/**
 * Created by alisle on 8/11/17.
 */
public class BTreeStoreTest {
    private static final Logger log = LoggerFactory.getLogger(BTreeStoreTest.class);

    private PersistenceHandler getHandler() throws Exception {
        final File tempDir = Files.createTempDir();
        tempDir.deleteOnExit();


        final PersistenceHandler handler = new PersistenceHandler(
                tempDir.getAbsolutePath(),
                248,
                100,
                100000,
                2);

        return handler;
    }

    @Test
    public void testAddGetSingle() throws Exception {
        int COUNT = 100000;
        long hashes[] = new long[COUNT];

        BTreeStore store = new BTreeStore( 1024, getHandler());

        log.debug("Adding Values");
        for(int x = 0; x < COUNT; x++) {
            String string = x + "";
            log.debug("Adding " + string);
            hashes[x] = store.add(string);
        }

        log.debug("Reading Values");
        for(int x = 0; x < COUNT; x++) {
            Optional<String> string = store.get(hashes[x]);
            Assert.assertTrue(string.isPresent());
            Assert.assertEquals(x + "", string.get());
        }

    }


}
