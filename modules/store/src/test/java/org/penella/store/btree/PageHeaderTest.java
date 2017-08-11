package org.penella.store.btree;

import org.junit.Assert;
import org.junit.Test;

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
