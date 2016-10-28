package org.penella.structures

import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
 * Created by alisle on 10/12/16.
 */
class SortedLinkList<T : Comparable<T>> {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(SortedLinkList::class.java)
    }

    class Node<T : Comparable<T>>(val value: Comparable<T>) {
        var next: Node<T>? = null
        var previous: Node<T>? = null
    }

    private var head:  Node<T>? = null
    private var tail: Node<T>? = null
    private var count: Long = 0

    var isEmpty : Boolean = head == null

    fun first(): Node<T>? = head
    fun last(): Node<T>? = tail
    fun length():Long = count

    fun pop() : Comparable<T>? {
        if(count > 0) {
            count--
            val node = head
            head = node?.next

            return node?.value
        }

        return null
    }

    fun remove() : Comparable<T>? {
        if(count > 0) {
            count--
            val node = tail
            tail = node?.previous

            return node?.value
        }

        return null
    }

    fun add(insertValue: T) {
        var found : Boolean = false
        var currentNode = head;

        if(head == null) {
            val insertNode = addNode(null, null, insertValue)
            tail = insertNode

            return
        }

        while(!found) {
            val currentValue = currentNode?.value
            when(currentValue?.compareTo(insertValue)){
                -1 -> {
                    if (currentNode == tail) {
                        val insertNode  = addNode(tail, null,  insertValue)
                        tail = insertNode
                        found = true
                    } else {
                        currentNode = currentNode?.next
                    }
                }
                0 -> found = true
                1 -> {
                    addNode(currentNode?.previous, currentNode, insertValue)
                    found = true
                }
            }
        }
    }

    private fun addNode(previous: Node<T>?, next: Node<T>?, value : T )  : Node<T> {
        count++

        val node = Node(value)
        node.previous = previous
        node.previous?.next = node
        node.next = next
        next?.previous = node

        if(next == head) {
            head = node
        }

        return node
    }

    fun get(where: Int) : Comparable<T>? {
        if( count > 0 && where < count) {
            val diff =  count - where
            if(diff < where) {
                if((where + 1L) == count) {
                    return tail?.value
                }

                var munch = count
                var node = tail;
                while(munch > where) {
                    node = node?.previous
                    munch--
                }

                return node?.value
            } else {
                var munch = 0
                var node = head
                while(munch < where) {
                    node = node?.next
                    munch++
                }

                return node?.value
            }
        }

        return null;
    }

}