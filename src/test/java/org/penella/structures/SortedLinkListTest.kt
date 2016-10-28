package org.penella.structures

import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

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
 * Created by alisle on 10/13/16.
 */
class SortedLinkListTest {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(SortedLinkListTest::class.java)
    }

    @Test
    fun testPushPop() {
        val list : SortedLinkList<Long> = SortedLinkList()
        list.add(3L)
        list.add(5L)
        list.add(2L)
        list.add(1L)
        list.add(4L)

        assertEquals(1L, list.pop())
        assertEquals(2L, list.pop())
        assertEquals(3L, list.pop())
        assertEquals(4L, list.pop())
        assertEquals(5L, list.pop())
    }

    @Test
    fun testAppendRemove() {
        val list : SortedLinkList<Long> = SortedLinkList()
        list.add(3L)
        list.add(5L)
        list.add(2L)
        list.add(1L)
        list.add(4L)

        assertEquals(5L, list.remove())
        assertEquals(4L, list.remove())
        assertEquals(3L, list.remove())
        assertEquals(2L, list.remove())
        assertEquals(1L, list.remove())
    }

    @Test
    fun testPerformance() {
        val max = 100000
        val list : SortedLinkList<Long> = SortedLinkList()
        var start = System.currentTimeMillis()

        log.warn("Adding " + max + " instances")
        for( x in 1L..max ) {
            if(x % 10000L == 0L) {
                log.warn("Adding " + x)

            }
            list.add(x)
        }
        log.warn("Finished in " + (System.currentTimeMillis() - start) / 1000 + " secs")

        start = System.currentTimeMillis()
        log.warn("Reading " + max + " instances")
        while(list.length() > 0) {
            list.pop()
        }
        log.warn("Finished in " + (System.currentTimeMillis() - start) / 1000 + " secs")

    }

}