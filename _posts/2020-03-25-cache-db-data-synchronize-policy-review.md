---
layout: post
title: Cach-DB data integrity policy review
tags: Database Cache 
categories: common
---

## CAP Theorem

CAP Theorem states that any distributed data store can only provide two of the following three guarantees:

- _Consistency_
Every read receives the most recent write or an error.
- _Availability_
Every request receives a (non-error) response, without the guarantee that it contains the most recent write.
- _Partition tolerance_
The system continues to operate despite an arbitrary number of messages being dropped (or delayed) by the network between nodes.

When a network partition failure happens, it must be decided whether to

cancel the operation and thus decrease the availability but ensure consistency or to
proceed with the operation and thus provide availability but risk inconsistency.

-- Wikipedia CAP Theorem


Based on that CAP theorem, so here we are going to talk about how to maintain the `Consistency` and `Partition Tolerance` between Cache and Database data where there is issue happened.

## Consistency

There are 3 type of Consistency:

1. `Strong Consistency`: All accesses are seen by all parallel processes (or nodes, processors, etc.) in the same order (sequentially)
        <br>Meaning: what we read is what someone write, 
2. `Weak Consistency`:  
        - All accesses to synchronization variables are seen by all processes (or nodes, processors) in the same order (sequentially) - these are synchronization operations. Accesses to critical sections are seen sequentially.<br>
        - All other accesses may be seen in different order on different processes (or nodes, processors).
        - The set of both read and write operations in between different synchronization operations is the same in each process.
        <br>Meaning: no guaranteed you will read the value after write, but will try best to provide the written data in shortest time
3. `Final Consistency` __THIS IS BEST WE CAN DO AND THIS WHAT WE ARE LOOKING FOR__
        Special type of `Weak Consistency`


## Final Consistency between Cache and DB -  Cache Policy

To ensure the `Final Consistency` between Cache and DB, We have following Cache Policy we can implement.

### 1. Cache-Aside

![picture 1](https://r0ngsh3n.github.io/static/img/../../../../../static/img/cache-aside.drawio.png)


####** Why __delete__ data in cache not __update__ data in cache?

1. Performance Consideration: if data in cache is calculated, then update might cause more calculation(maybe joint on other set of data etc.), which is expensive, just delete is cheapest way and also fit `Lazy Initialize` policy.
<br>
2. Race Condition while update Data: if Thread1 suppose update first and Thread2 update second, Thread1 should complete 2 steps (see picture above) as atomic process before Thread2 start, but because the network reason, after Thread1 update database, Thread2 start, and Threa2 complete Update Database and update cache earlier than Thread1, then the data in cache is Thread1 data not Thread2 data.

####** Why delete database __first__, not Cache data?

![picture 2](https://r0ngsh3n.github.io/static/img/../../../../../static/img/cache-aside-erro1.drawio.png)

In the above Pciture

1. Thread 1 is write request start first: 1. Delete data in Cache 
2. Thread 2 is read request and come next: 2. Query Data in Cache, and data is not in Cache - delete by Thread 1 - so go DB and read data from there
3. Thread 1 now is going to add new data in DB - DB data is updated to the latest data
4. Thread 2 after read the data from DB, now <u>update cache with the data it read from DB, which is NOT latest data Thread 1 just write in</u>

5. Now the Data and Cache is out of sync...

####** Fuck it! I just want to delete Cache first then update DB, can I?
Yes, you can. But you need `Double Delete` strategy


![picture 3](https://r0ngsh3n.github.io/static/img/../../../../../static/img/double-delete-cache.drawio.png)

in the write request - `Sleep for some time` that time ususally is little bit bigger than the read request time expense.


####** So...is Cache-side Policy guarantee "Consistence" between DB and Cache?

__No!!!!!__

![picture 4](https://r0ngsh3n.github.io/static/img/../../../../../static/img/cache-aside-erro2.png)

