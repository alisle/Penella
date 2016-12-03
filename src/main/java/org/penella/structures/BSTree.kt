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
 * Created by alisle on 10/17/16.
 */

open class BSTreeNode<I : Comparable<I>, P>(val index : I, var payload : P ) {
    var parent: BSTreeNode<I, P>? = null
    var left: BSTreeNode<I, P>? = null
    var right: BSTreeNode<I, P>? = null
}



open class BSTree<I : Comparable<I>, P> {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(BSTree::class.java)
    }


    private var root : BSTreeNode<I,P>? = null

    private fun add(parent: BSTreeNode<I, P>?, index: I, payload : P, replace : Boolean = true) : P {
        var node = parent
        var found : Boolean = false

        while(!found) {
            when(index.compareTo(node!!.index)) {
                -1 -> {
                    if(node?.left != null) {
                        node = node!!.left
                    } else {
                        node?.left = BSTreeNode(index, payload)
                        node?.left?.parent = node
                        return payload
                    }
                }
                0 -> {
                    when(replace) {
                        true -> {
                            if(log.isDebugEnabled) log.debug("Replacing $index")
                            node!!.payload = payload
                            return payload
                        }
                        false -> {
                            return node!!.payload
                        }
                    }
                }
                1 -> {
                    if(node?.right != null) {
                        node = node!!.right
                    } else {
                        node?.right = BSTreeNode(index, payload)
                        node?.right?.parent = node
                        return payload
                    }
                }
            }
        }

        return payload
    }

    fun addOrGet(index: I, payload: P) : P {
        if(log.isTraceEnabled) log.trace("Getting or Adding index: $index, with payload: $payload")

        when(root) {
            null -> {
                root = BSTreeNode(index, payload)
                return payload
            }
            else -> {
                return this.add(root, index, payload, false)
            }
        }
    }

    fun add(index: I, payload: P) {
        if(log.isTraceEnabled) log.trace("Adding index: $index, with payload: $payload")

        if(root == null) {
            root = BSTreeNode(index, payload)
        } else {
            this.add(root, index, payload)
        }
    }

    fun get(index: I) : P? {
        var parent = root
        while(parent != null) {
            when (index.compareTo(parent.index)) {
                -1 -> parent = parent.left
                0 -> return parent.payload
                1 -> parent = parent.right
            }
        }

        return null
    }
    private fun all(root : BSTreeNode<I, P>, list : MutableList<Pair<I,P>>)  {
        if(root.left != null) {
            all(root.left!!, list)
        }

        list.add(Pair(root.index, root.payload))

        if(root.right != null) {
            all(root.right!!, list)
        }

    }

    fun all() : List<Pair<I,P>>
    {
        val list : MutableList<Pair<I,P>> = mutableListOf()

        if(root != null) {
            all(root!!, list)
        }

        return list
    }

    fun allPayloads() : List<P> {
        return all().map { x -> x.second }
    }

}