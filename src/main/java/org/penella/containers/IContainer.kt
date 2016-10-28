package org.penella.containers

import org.penella.structures.BSTree

/**
 * Created by alisle on 10/28/16.
 */
interface IContainer {
    fun put( first : Long, second: Long, third: Long)
    fun get( first: Long, second: Long) : BSTree<Long>
    fun get( first: Long) : BSTree<BSTree<Long>>
}