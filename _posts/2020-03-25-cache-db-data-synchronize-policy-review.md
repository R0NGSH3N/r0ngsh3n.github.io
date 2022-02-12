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


** Why __delete__ data not __update__ data in cache?

1. Performance Consideration: if Data in cache is calculated, then update might cause more calculation, which is expensive, just delete is cheapest way and also fit `Lazy Initialize` policy.
<br>
2. Race Condition while update Data: if Thread1 suppose update first and Thread2 update second, but because the network reason, Thread2 update first, then Thread1, then this cause the data in Cache is not latest.

** Why delete database __first__, not Cache data?


![picture 2](https://r0ngsh3n.github.io/static/img/../../../../../static/img/cache-aside-erro1.drawio.png)