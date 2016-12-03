package org.penella.index

/**
 * Created by alisle on 11/1/16.
 */
interface IIndexFactory {
    fun createIndex(index: IndexType) : IIndex
}