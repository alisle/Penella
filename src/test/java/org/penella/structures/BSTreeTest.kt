package org.penella.structures

import net.openhft.hashing.LongHashFunction
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
 * Created by alisle on 10/17/16.
 */
class BSTreeTest {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(BSTreeTest::class.java)
    }

    @Test
    fun testPerformance() {
        val seed : Long = 665445839
        val max = 10000000
        val tree : BSTree<Long, String> = BSTree()
        var start = System.currentTimeMillis()

        log.warn("Starting the creation of the tree")
        for(x  in 1L..max) {
            val string = "" + x
            val hash = LongHashFunction.xx_r39(seed).hashChars(string)
            if((x % 1000000L) == 0L) {
                log.debug("Added " + x)
            }
            tree.add(hash, string)
        }
        var finish = (System.currentTimeMillis() - start)
        var average : Double = max / (finish / 1000.0)

        log.warn("Finished in " + finish / 1000 + " secs")
        log.warn("Average insert time: " + average + " recs / sec")

        start = System.currentTimeMillis()
        log.warn("Retrieving " + max + " instances")
        for( x in 1L..max) {
            val string = "" + x
            val hash = LongHashFunction.xx_r39(seed).hashChars(string)
            val oldString = tree.get(hash)
            assertEquals(string, oldString)
        }

        finish = (System.currentTimeMillis() - start)
        average = max / (finish / 1000.0)

        log.warn("Finished in " + finish / 1000 + " secs")
        log.warn("Average Fetch time: " + average + " recs / sec")

    }
}