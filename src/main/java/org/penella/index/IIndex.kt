package org.penella.index

import org.penella.query.IncompleteResultSet
import org.penella.structures.triples.HashTriple
import org.penella.structures.triples.TripleType

/**
 * Created by alisle on 9/27/16.
 */

interface IIndex {
    fun add(triple: HashTriple) 
    fun get(first: TripleType, value: Long) : IncompleteResultSet
    fun get(first: TripleType, second: TripleType, firstValue: Long, secondValue: Long) : IncompleteResultSet

}
