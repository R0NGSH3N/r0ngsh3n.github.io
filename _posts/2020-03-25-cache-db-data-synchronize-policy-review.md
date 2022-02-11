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
        Meaning: what we read is what someone write, 
2. `Weak Consistency`:  
        - All accesses to synchronization variables are seen by all processes (or nodes, processors) in the same order (sequentially) - these are synchronization operations. Accesses to critical sections are seen sequentially.<br>
        - All other accesses may be seen in different order on different processes (or nodes, processors).
        - The set of both read and write operations in between different synchronization operations is the same in each process.
        Meaning: no guaranteed you will read the value after write, but will try best to provide the written data in shortest time
3. `Final Consistency` __THIS IS BEST WE CAN DO AND THIS WHAT WE ARE LOOKING FOR__
        Special type of `Weak Consistency`


## Final Consistency between Cache and DB -  HOW? 

### Cache Policy