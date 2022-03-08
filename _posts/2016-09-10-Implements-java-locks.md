---
layout: post
title: Understanding Java Locks
tags: Java MultiThread Lock
categories: common
---

Understand Java Locks from following 4 aspects:

1. Where you store "who will own the lock" information. You can store in class, instance or lock itself.
    `sychronized` store the lock information in current class or piece of it
    `ReentryLock` store the lock information in the Lock.
    `Semaphore` store the lock information in the Lock itself.

2. The rule of who can acquire lock:
    `Mutex`: only 1 thread can acquire the lock
    `Semaphore`: only limited threads can acquire Lock
    `ReentryLock`: a thread can multiple time acquire the lock
    `Read-Write Lock`: read can share the resource, write is `Mutex` lock.

3. What you gonna do when you can NOT acquire lock
    `tryLock` if you don't get the lock, exit.
    `tryLock(3, Time.SECONDS`) if you don't get the lock, wait for 3 seconds and then exit.
    `lock()` you block the thread and wait for the lock.

4. Fairness - when a lock released, who can get lock
        `Fair` : JVM need to maintain a queue to make sure waiting threads FIFO - this is good but more resource expend
        `UnFair`: once the Lock released, anyone is possible to get the lock in the pool. This could be resource expensive if you use `lock()` method, because in Java, this will trigger the thread `spin`, that is CPU intense process. Which `synchronized` is typical `UnFair` lock.

`CAS`: Compute And Swap

`CAS` is `Optimistic` locking (compare with `pessimistic` locking). It basically do 2 things when lock/unlock.

1. Before it lock, it obtain atomic number in memory (__V__) and record the current value (__A__).
2. When try to lock, it check the value of __V__ is STILL equal to __A__? if yes, set __V__ value to __B__, and return true - meaning lock successful, if not, then return false meaning lock failed. - Compute And Swap

Be aware that the ``Compute And Swap` is CPU level of atomic process (it is called `CAH` instruct ) , in Java it use `sun.misc.Unsafe` class.

`AQS`: Abstract Queued Synchronizer

`AQS` provided a Queue type data structure (`State` and `Node` - (vale, prevNode and nextNode ) ) that could be used in Lock mechanism. The `Lock`, `ReentryLock`, `Semaphore` and all the `Atomic` classes are based on the `AQS` Framework.
