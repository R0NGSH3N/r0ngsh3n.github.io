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
    `Semaphore` store the lock informatoin in the Lock itself.

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
        `UnFair`: once the Lock released, anyone is possible to get the lock in the pool.