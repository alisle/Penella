package org.penella.store.btree;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;
import org.penella.store.btree.InternalEntry;
import org.penella.store.btree.Page;
import org.penella.store.btree.PageHeader;
import org.penella.store.btree.PersistenceHandler;

import java.io.File;

/**
 * Created by alisle on 8/10/17.
 */
public class InternalEntryTest {

    @Test
    public void testGetters() {
        final File tempDir = Files.createTempDir();
        tempDir.deleteOnExit();

        String directory = tempDir.getAbsolutePath();
        PersistenceHandler handler = new PersistenceHandler(directory, 1024, 10, 100, 2);

        Page page = new Page(handler, 10, false);
        PageHeader header = page.getHeader();
        InternalEntry entry = new InternalEntry(header, 1001, page);

        Assert.assertEquals(1001, entry.getKey());
        Assert.assertEquals(page, entry.getNext());
        Assert.assertEquals(header, entry.getHeader());
    }
}
