package org.penella.structures

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by alisle on 10/12/16.
 */
class LinkedList<T> {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(LinkedList::class.java)
    }

    class Node<T>(val value: T) {
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

    fun push(value: T) {
        val node : Node<T> = Node(value)
        node.next = head
        head?.previous = node
        head = node

        if(tail == null) {
            tail = head
        }

        count++
    }

    fun add(value: T) {
        val node : Node<T> = Node(value)
        node.previous = tail
        tail?.next = node
        tail = node

        if(head == null) {
            head = tail
        }

        count++
    }

    fun pop() : T? {
        if(count > 0) {
            count--
            val node : Node<T>? = head
            head = node?.next
            return node?.value
        }

        return null
    }

    fun get(where: Int) : T? {
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
    fun remove() : T? {
        if(count > 0) {
            count--
            val node : Node<T>? = tail
            tail = node?.previous
            return node?.value

        }

        return null
    }

}