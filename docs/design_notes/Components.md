# Component breakdown
<!!IMAGE>

###Componenets
####Node
Each JVM instance has one node, the node contains one store and a set of databases

####Store
Each node keeps one store. The store stores all the strings within all the DBs. The store hashes each string using xxHash and compresses the string using a shared dictionary,
the stores the results within a BST

####Database
The DB is a self contained RDF DB. The set is broken up into a set of shards using the hash of the subject.

####Shard
Shards contains a set of containers. There is a container for each of the indexes that are kept. There is an index for Object, Property-Object, Subject-Object and finally Subject-Property-Object

####Container
The container has a series of BST as shown: <!!IMAGE>
