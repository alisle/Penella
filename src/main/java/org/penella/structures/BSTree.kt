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
class BSTree<T> {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(BSTree::class.java)
    }

    class Node<T>(val index : Long, var payload : T ) {
        var left : Node<T>? = null
        var right: Node<T>? = null
        val parent: Node<T>? = null
    }

    private var root : Node<T>? = null

    private fun add(parent: Node<T>?, index: Long, payload : T) {
        var node = parent
        var found : Boolean = false

        while(!found) {
            when(index.compareTo(node!!.index)) {
                -1 -> {
                    if(node?.left != null) {
                        node = node!!.left
                    } else {
                        node?.left = Node(index, payload)
                        found = true
                    }
                }
                0 -> {
                    log.debug("Replacing $index")
                    node!!.payload = payload
                    found = true
                }
                1 -> {
                    if(node?.right != null) {
                        node = node!!.right
                    } else {
                        node?.right = Node(index, payload)
                        found = true
                    }
                }
            }
        }
    }

    fun add(index: Long, payload: T) {
        if(log.isTraceEnabled) {
            //log.trace("Adding index: $index, with payload: $payload")
        }

        if(root == null) {
            root = Node(index, payload)
        } else {
            this.add(root, index, payload)
        }
    }

    fun get(index: Long) : T? {
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
}