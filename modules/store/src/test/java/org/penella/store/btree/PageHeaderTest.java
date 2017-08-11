package org.penella.store.btree;

import org.junit.Assert;
import org.junit.Test;
import org.penella.store.btree.PageHeader;

import java.util.UUID;

/**
 * Created by alisle on 8/10/17.
 */
public class PageHeaderTest {

    @Test
    public void testGetters() {
        UUID uuid = UUID.randomUUID();
        PageHeader header = new PageHeader(uuid, true, 10);
        Assert.assertEquals(uuid, header.getUuid());
        Assert.assertEquals(true, header.isExternal());
        Assert.assertEquals(10, header.getMaxPageSize());
    }

    @Test
    public void testCurrentItems() {
        PageHeader header =  new PageHeader(UUID.randomUUID(), true, 10);
        Assert.assertEquals(0, header.getCurrentSize());
        header.incrementCurrentSize();

        Assert.assertEquals(1, header.getCurrentSize());
        header.incrementCurrentSize();
        Assert.assertEquals(2, header.getCurrentSize());

        header.setCurrentSize(5);
        Assert.assertEquals(5, header.getCurrentSize());
    }
}
