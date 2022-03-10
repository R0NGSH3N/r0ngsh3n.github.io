---
layout: post
title: Understanding Java Locks
tags: Java MultiThread Lock
categories: common
---
### Understand Java Locks from following 4 aspects:

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

    `Fair` : JVM need to maintain a queue to make sure waiting threads FIFO - this is good but more resource expend, so JVM think `Fair` lock is always more expensive than `UnFair`, so default all the lock is `UnFair`

    `UnFair`: once the Lock released, anyone is possible to get the lock in the pool. From System Design perspective, `UnFair` is always 'cheaper' than `Fair`, but actually in real world, it is hard to tell - because if you implemented the `Spin` lock like if you put `tryLock` in while loop, then instead of the thread `park`, it will spin until lock get free, that `spin` process of CPU intense process.  

### `Optimistic` vs `Pessimistic` lock

`Optimistic` lock is thought that when there are multi threads access same resource, we "assume" there should be no concurrent issue, so no need to lock - `CAS` is implementation of `Optimistic`

`Pessimistic` lock is thought that every time there is locking issue when multi threads access the resource, so you should `exclusive` lock the resource every time - `synchronized` is implementation of `Pessimistic`.

So, when you have situation that __Read more than Write__, you should use `Optimistic` lock, and if __Write more than Read__, then you should use `Pessimistic` lock.

### `CAS`: Compute And Swap

`CAS` is `Optimistic` locking (compare with `pessimistic` locking). It basically do 2 things when lock/unlock.

1. Before it lock, it obtain atomic number in memory (__V__) and record the current value (__A__).
2. When try to lock, it check the value of __V__ is STILL equal to __A__? if yes, set __V__ value to __B__, and return true - meaning lock successful, if not, then return false meaning lock failed. - Compute And Swap

Be aware that the `Compute And Swap` is CPU level of atomic process (it is called `CAH` instruct ) , in Java it use `sun.misc.Unsafe` class, and underneath Java use C to provide lock.

But `CAS` is not always thread-safe, it has potential issue called `ABA` issue:

if Thread 1: `T1` get the variable __V__, and the value of __V__ is __A__, and Thread 2: `T2` also get variable __V__ and value of __V__ is __A__, after while, `T2` updated the __V__ to value __B__, _but for some reason, `T2` updated V back to value A_, in this case, when `T1` compare the value of __V__, `T1` thought there is no change, so `T1` will overwrite `T2`'s change.

### `AQS`: Abstract Queued Synchronizer

`AQS` provided a Queue type data structure (`State` and `Node` - (vale, prevNode and nextNode ) ) that could be used in Lock mechanism. The `Lock`, `ReentryLock`, `Semaphore` and all the `Atomic` classes are based on the `AQS` Framework.

here is the pseudo code for `AQS`:

~~~java
class AbstractQueuedSynchronizer {
  volatile Node head;  
  volatile Node tail;  
  volatile int state; 
}

class Node {
  Node prev;
  Node next;
  Thread thread; 

  Node nextWaiter; 
  int waitStatus; 
}

~~~

The `AQS` maintain the `head` and `tail`, when the lock is free, the `head` thread will be wake up(unpark), and if a thread failed to acquire lock, it will be set as `tail`.

Each `Node` Object has `prev` and `next`, which if `Fair` lock, then there is another method called `hasQueuedPredecessors()`, if implement this method while try to lock, then it will be `Fair` lock. 