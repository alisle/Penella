# Component breakdown
![Component Diagram](https://github.com/alisle/Penella/blob/master/docs/img/components.png)

###Componenets
####Node
Each JVM instance has one node, the node contains one store and a set of databases

####Store
Each node keeps one store. The store stores all the strings within all the DBs. The store hashes each string using xxHash and compresses the string using a shared dictionary,
the stores the results within a BST

####Database
The DB is a self contained RDF DB. The set is broken up into a set of shards using the hash of the subject.

####Shard
 Shards contains a set of Indexes. The triples are kept in 4 different indexes to allow fast retrieval. At this point the triples are a series of 3 Longs kept in memory.

####Index
The index is a BST of BSTs, this allows for fast retrieval of partial sets of data. The setup is as shown: <!!IMAGE>
