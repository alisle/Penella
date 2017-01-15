package org.penella.structures

import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by alisle on 10/12/16.
 */
class LinkedListTest {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(LinkedListTest::class.java)
    }

    @Test
    fun testEmptyGet() {
        val list : LinkedList<Long> = LinkedList()
        assertNull(list.get(0))
    }

    @Test
    fun testNegativeEmptyGet() {
        val list : LinkedList<Long> = LinkedList()
        assertNull(list.get(-10))
    }

    @Test
    fun testGet() {
        val list : LinkedList<Long> = LinkedList()
        list.add(1L)
        list.add(2L)
        list.add(3L)
        list.add(4L)

        assertEquals(1L, list.get(0))
        assertEquals(2L, list.get(1))
        assertEquals(3L, list.get(2))
        assertEquals(4L, list.get(3))
    }

    @Test
    fun testPushPop() {
        val list : LinkedList<Long> = LinkedList()
        list.push(1L)
        list.push(2L)
        list.push(3L)

        assertEquals(3L, list.pop())
        assertEquals(2L, list.pop())
        assertEquals(1L, list.pop())
    }

    @Test
    fun testAppendRemove() {
        val list : LinkedList<Long> = LinkedList()
        list.add(1L)
        list.add(2L)
        list.add(3L)

        assertEquals(3L, list.remove())
        assertEquals(2L, list.remove())
        assertEquals(1L, list.remove())
    }

    @Test
    fun testRandomGet() {
        val max = 1000
        val list : LinkedList<Long> = LinkedList()
        var start = System.currentTimeMillis()

        log.warn("Adding " + max + " instances")
        for( x in 1L..max ) {
            list.push(x)
        }
        log.warn("Finished in " + (System.currentTimeMillis() - start) / 1000 + " secs")

        val shuffle = (1L..max).toList()
        Collections.shuffle(shuffle)

        log.warn("Getting " + max + " instances")
        shuffle.forEach {
            list.get(it.toInt())
        }
        log.warn("Finished in " + (System.currentTimeMillis() - start) / 1000 + " secs")



    }
}

