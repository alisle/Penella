package org.penella.store.btree;

import org.junit.Assert;
import org.junit.Test;
import org.penella.store.btree.ExternalEntry;
import org.penella.store.btree.PageHeader;

import java.util.UUID;

/**
 * Created by alisle on 8/10/17.
 */
public class ExternalEntryTest {

    @Test
    public void testGetters() {
        PageHeader header = new PageHeader(UUID.randomUUID(), true, 10);
        ExternalEntry entry = new ExternalEntry(header, 1001, 10001);
        Assert.assertEquals(1001, entry.getKey());
        Assert.assertEquals(10001, entry.getPosition());
        Assert.assertEquals(header, entry.getHeader());
    }

}
