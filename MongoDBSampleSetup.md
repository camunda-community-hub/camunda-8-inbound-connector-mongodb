[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
[![](https://img.shields.io/badge/Lifecycle-Proof%20of%20Concept-blueviolet)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#proof-of-concept-)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)

# Sample MongoDB Setup

To take advantage of the Change Stream feature of MongoDB, it needs to be deployed in a Replica Set or Sharded Cluster topology rather than in a standalone configuration. Typically, this requires several servers but for testing and development purposes, it can be deployed to a single server. The following is a guide to installing and deploying MongoDB in a Replica Set topology. 

## Install MongoDB Community Edition and MongoDB Compass

Download and install MongoDB Community Edition. Follow the instructions detailed [here] based on your operating system.

Download and install MongoDB Compass. Compass is MongoDB's client to simplify database administration.

## Set up Replica Set topology

For a basic Replica Set you'll need to create three Mongo DB nodes and then tie them together. We won't be setting up security but it will be added in future iterations.

First, create three directories, one for each MongoDB node. In this example they are located in 

```/Users/{user}/data/db1```

```/Users/{user}/data/db2```

```/Users/{user}/data/db3```

Next, create three configuration files for each of the nodes. The location of the default configuration file depends on how MongoDB was installed. Check (link)
to determine where yours is located. This example used Homebrew and as a result the configuration file is located here:

```/opt/homebrew/etc/mongod.conf```

Make a copy, name it ```mongod1.conf``` and update the systemLog file name to ```mongo1.log```, update the dbpath to point to the ```db1``` directory, and define the replica set name, in this example ```rs0``` 
```
systemLog:
  destination: file
  path: /opt/homebrew/var/log/mongodb/mongo1.log
  logAppend: true
storage:
  dbPath: /Users/{user}/data/db1
replication:
  replSetName: rs0
net:
  bindIp: 127.0.0.1
  port: 27017
  ipv6: true
```

Make a copy of ```mongod1.conf```, name it ```mongod2.conf``` and update the systemLog file name to ```mongo2.log```, update the dbpath to point to the ```db2``` directory, and update the port to ```27018```
```
systemLog:
  destination: file
  path: /opt/homebrew/var/log/mongodb/mongo2.log
  logAppend: true
storage:
  dbPath: /Users/{user}/data/db2
replication:
  replSetName: rs0
net:
  bindIp: 127.0.0.1
  port: 27018
  ipv6: true
```
Make another copy of ```mongod1.conf```, name it ```mongod3.conf``` and update the systemLog file name to ```mongo3.log```, update the dbpath to point to the ```db3``` directory, and update the port to ```27019```
```
systemLog:
  destination: file
  path: /opt/homebrew/var/log/mongodb/mongo3.log
  logAppend: true
storage:
  dbPath: /Users/{user}/data/db3
replication:
  replSetName: rs0
net:
  bindIp: 127.0.0.1
  port: 27019
  ipv6: true
```

Next, invoke
```mongosh```
to start a MongoDB shell session.

Issue the following command to initiate the replica set:
```
rs.initiate( {
    _id : "rs0",
    members: [
       { _id: 0, host: "localhost:27017" },
       { _id: 1, host: "localhost:27018" },
       { _id: 2, host: "localhost:27019" }
    ]
 })
```

## Start MongoDB

Open a terminal window and issue the following command to start the first node. Be sure to point to the first config file:
```
mongod --config /opt/homebrew/etc/mongod1.conf
```

Open a second terminal window or tab and issue the following command to start the second node. Be sure to point to the second config file:
```
mongod --config /opt/homebrew/etc/mongod2.conf
```

Open a third terminal and issue the following command to start the third node. Be sure to point to the third config file:
```
mongod --config /opt/homebrew/etc/mongod3.conf
```

The nodes should find each other and set itself up as a replica set.

## Start Compass, create a database and a collection

Start MongoDB Compass and create a new connection. Use the following URI to connect to the replica set:

```mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0```

You should see the default databases. Create a database and a collection within the database. This example used ```testdb``` and ```testcollection```.

Before you begin to add data, be sure to start the Inbound Connector.

As data is added, replaced, or deleted you should see log statements being generated and if there are any MongoDB Inbound Connectors deployed you should also see process instances start or messages sent. 
