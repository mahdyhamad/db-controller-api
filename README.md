## NoSQL Database Implementation Optimized for Extensive Read Operations

### 1. The Design
An overview of the database design shows the main components.

![sticker](https://github.com/mahdyhamad/db-controller-api/blob/main/docs/images/Design%20Overview.jpg)


#### 1.1 Primary Server

Starting with the Primary Server which has several responsibilities. The primary server is responsible for writing to the database, which involves the creation, updating, and deletion of collections, documents, and databases. For each write operation, the Primary Server is responsible for notifying RORs (Read-Only Replicas) of write operations. This will make sure the data is synced. When a write occurs on the host file system, the virtual file system of the docker runtime is immediately updated. That is because the docker runtime database directory is bind-mounted to the host database directory using docker volumes. This enables immediate synchronization between host and docker runtime database directories. 

Another responsibility of the primary server is providing read-only connections to clients. In addition to that, the primary server provides authentication and authorization. And the primary server is responsible for scaling the database horizontally.

#### 1.2 ROR - Read-Only Replica

This server is built for optimized read operations. It is designed to handle millions of documents and to look for a document in milliseconds time. The main responsibility of this server is to provide data to clients as fast as possible. It has the ability to fetch data from the file system directly, but that should happen rarely in cases it does not find what it is looking for in memory. 
 
#### 1.3 Server Pool

The server pool represents the place where all read-only replicas live. 

### 2. Communication Protocol
All components communicate using HTTP protocol. There are two ways communication can occur: between the client and the database, and between the database servers. 



### 3. Authentication and Authorization
#### 3.1 JWT Authentication
JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.

Although JWTs can be encrypted to also provide secrecy between parties, we will focus on signed tokens. Signed tokens can verify the integrity of the claims contained within it, while encrypted tokens hide those claims from other parties. When tokens are signed using public/private key pairs, the signature also certifies that only the party holding the private key is the one that signed it.

In authentication, when the user successfully logs in using their credentials, a JSON Web Token will be returned. Since tokens are credentials, great care must be taken to prevent security issues. In general, you should not keep tokens longer than required.
Whenever the user wants to access a protected route or resource, the user agent should send the JWT, typically in the Authorization header using the Bearer schema.

#### 3.2 ACL Authorization
an access-control list (ACL) is a list of permissions associated with a system resource (object). An ACL specifies which users or system processes are granted access to objects, as well as what operations are allowed on given objects. Each entry in a typical ACL specifies a subject and an operation. For instance, if a file object has an ACL that contains (Alice: read,write; Bob: read), this would give Alice permission to read and write the file and only give Bob permission to read it.

A filesystem ACL is a data structure (usually a table) containing entries that specify individual user or group rights to specific system objects such as programs, processes, or files. These entries are known as access-control entries (ACEs). Each accessible object contains an identifier to its ACL. The privileges or permissions determine specific access rights, such as whether a user can read from, write to, or execute an object. In some implementations, an ACE can control whether or not a user, or group of users, may alter the ACL on an object.



### 4. CRUD Operations
Similar to typical databases, the database used in this project has 4 main operations: create, read, update, and delete. Write operations: create, update, and delete are the responsibility of the primary server. To ensure a centralized data source, all write operations can be done through a single component. This makes it easy to notify RORs. 

For read operations, all documents are loaded to the RAM for fast reading. Read operations can be done using indexed fields. Querying using these fields provides fast querying time even when there are 10s of millions of records. But trying to get data using un-indexed fields will be much slower. Because the database should go through all records in the collection until it finds the document. This is not recommended frequently due to its expensive lookup time. 



### 5. Indexing
Indexing is implemented using a binary search tree. This implementation provides fast reads for indexed fields and fast insertion and deletion. The index can be found only in RORs. Only single indexed fields are provided in this implementation.


### 6. Scalability
NoSQL databases have an advantage over relational databases when it comes to scaling. NoSQL databases can be scaled horizontally, which is cheaper and efficient. Horizontal scaling is done by the Primary Server. Using docker, a ROR image is built, run, and added to the pool of many RORs. When a read-only replica starts, it loads the data from the database directory and loads all databases and index fields on the memory. Exposing a random port to the host enables clients to start connections immediately. 




### 7. File Storage
Data should be stored on disk in some format, and we should be able to retrieve that data. In this project, JSON files are used to store documents. Each collection has a file ending in .json. The reason .json files are used is due to its good representation of documents, which makes it easy to load data on memory and as a result less processing overhead. Parsing a JSON file is done by a package called com.googlecode.json-simple. Not to mention, the performance of parsing a json file depends on the specific implementation and algorithms used by the library. What we look for in a JSON parser, is to be able to handle small and large files efficiently. And after further experimenting and research, the implementation of the json-simple package gives us the best performance dealing with small and large files compared to some other json parsing packages. 


### 8. Load-Balancing
It is important to have a load balancer to make sure resources are utilized correctly and to avoid overloading on some resources and underload in others. Because we have only one server responsible for write operations, the load balancing is implemented for RORs. For this implementation, a static load balancing algorithm is used. This means that without knowing the current state of each read-only replica, the load is distributed using a random approach. For that, Round robin load balancing distributes traffices to a list of servers located in the server pool by rotation.



### 9. Data Structures
#### 9.1 Binary Search Tree
Binary Search Tree is a node-based binary tree data structure which has the following properties:
The left subtree of a node contains only nodes with keys lesser than the node’s key.
The right subtree of a node contains only nodes with keys greater than the node’s key.
The left and right subtree each must also be a binary search tree.
BST is used in this project to store indexed fields in memory. BST enables us to 
