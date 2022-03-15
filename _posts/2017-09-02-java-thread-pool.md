---
layout: post
title: Java Thread Pool How to
tags: java Multi-Thread Thread-Pool Executor 
categories: common
---

## `ThreadPoolExecutor`

I would say prefer to use `ThreadPoolExecutor`. 

~~~java
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.*;

public class TestThreadPoolExecutor {

    public static void main(String[] args) {
        RejectedExecutionHandler executionHandler = (r, executor) -> {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RejectedExecutionException("Producer thread interrupted", e);
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10, //corePoolSize
                20, //maxPoolSize
                100, //keepLiveTime
                TimeUnit.SECONDS, //TimeUnit
                new LinkedBlockingDeque<>(), //workQueue
                new ThreadFactoryBuilder().setNameFormat("your-thread-name-%d").build(), //ThreadFactory
                executionHandler //RejectedExecutionHandler);
    }
}
~~~

full test code are [here](https://github.com/R0NGSH3N/playground/blob/main/src/main/java/com/justynsoft/da/threads/TestThreadPoolExecutor.java)

### `ThreadPoolExecutor` creation

1. core Pool Size: When `ThreadPoolExecutor` instantiated, it will NOT create any thread until first time it get called or someone run the `prestartAllCoreThreads()`, it will create thread one-by-one until reached core Pool Size number.

2. max Pool Size: when the `Queue` is full, then `ThreadPoolExecutor` will create new thread until reach Max Pool Size then JVM will run the Rejected Policy. So the process is like this:

    when someone request new thread:
       1. check if current pool size < core pool size? yes, then create new thread, end of the story.
       2. If the current pool size reached the core pool size, then check if the Queue is full or not, if not, then add thread to the queue, if Queue is full, and pool size is not reach the max pool size, create new thread, otherwise, trigger the reject policy.

3. keep live time: This parameter only works when the pool size is more than core pool size, then JVM will check each 'extra' thread, if not in use for certain time (= keep live time), JVM will recycle these threads.

4. Time Unit: the keep live time time unit.

5. Work Queue: When pool size reach core pool size, JVM will add thread into this queue.

6. ThreadFactory: Provide Thread to `ThreadPoolExecutor`.

7. Rejected Execution handler: when max pool size reached, JVM will reject the request, and trigger the rejected policy, there are 4 default reject policy:

    ThreadPoolExecutor.AbortPolicy: throw RejectedExecutionException
    ThreadPoolExecutor.DiscardPolicy： dump the task, but no exception throw
    ThreadPoolExecutor.DiscardOldestPolicy： dump the first task in the queue, then retry.
    ThreadPoolExecutor.CallerRunsPolicy：let caller thread handle this task

~~~java
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            20,
            100,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(10),
            new ThreadFactoryBuilder().setNameFormat("your-thread-name-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

~~~

you also can customize the rejected policy:

~~~java
RejectedExecutionHandler executionHandler = (r, executor) -> {
   try {
​     executor.getQueue().put(r);
   } catch (InterruptedException e) {
​     Thread.currentThread().interrupt();
​     throw new RejectedExecutionException("Producer thread interrupted", e);
   }
 };
~~~

in the above `RejectedExecutionHandler`, we defined that if my thread get rejected by executor, I will call the queue, and use `put()` method (not offer()) to try add myself into the queue - the `put()` method is `blocking` method, it is not nice but probably simplest way to handle.

Use extra storage to store the thread - eg. MQ if your task get rejected by `ThreadPoolExecutor`

You can use default `ThreadPoolExecutor.AbortPolicy`, in this case, a `RejectedExecutionException` will be throw and you can catch that exception and put the thread in some where for later retry.

the biggest issue of using `Executor` to create Thread pool is the `max` number of thread is limited to `Integer.MAX_VALUE` regardless using `FixedThreadPool` or `CachedThreadPool`, and `ThreadPoolExecutor` also provide more control on how to set up the pool, but when you set up, you need consider following questions:

### `ThreadPoolExecutor` life cycle

After you create `ThreadPoolExecutor`, there is no aync daemon process start immediately, meaning, the `ThreadPoolExecutor` will not create any threads in the pool, so you have have that creation in main method, it will quit.

You can start the `ThreadPoolExecutor` without wait for some one send request - even this is prefer as 'Lazy-Initialization'. When you call `prestartCoreThread()` or `prestartAllCoreThreads()` methods, the `TreadPoolExecutor` daemon will start. In that case, until you call `shutdown` method the daemon will not stop.

There are 2 methods of shutdown the `ThreadPoolExecutor`: 

`shutdown()`: Will not shut down the executor immediately, it will wait all the executor finish the job and then shutdown.
`shutdownNow()`: Will shut down the executor immediately.

### Some thoughts when you use `ThreadPoolExecutor`

- you need think about the `core` thread number and `max` thread number. some tasks are 'CPU' intense, and some are 'I/O' intense. For 'CPU' intense tasks, you need set up low `core` and `max` thread number, but for `I/O` intense tasks, you can set up high number of `core` and `max` thread, because CPU have more resource than IO, and it can work more thread. 

Usually I will set `max` thread number double of `core` number, and set the `Queue` length same as `max`.

- How to create `ThreadFactory`, I would prefer to use guava - `ThreadFactoryBuilder` to create:

~~~java
    new ThreadFactoryBuilder().setNameFormat("your-thread-name-%d").build();
~~~

- What type of Queue you will use

In all the Thread Pool that `Executor` generate, it always use `LinkedBlockingQueue`, when using `BlockingQueue`, please notice when you add a thread into a full `LinkedBlockingQueue` (by using offer()), JVM will NOT `blocking` you, it will trigger the `Rejection Policy` immediately.But there are other queue available for use:

ArrayBlockingQueue
LinkedBlockingQueue
SynchronousQueue

- What `Rejection Policy` you want to use

When the `Queue` is full, it will trigger the `Rejection Policy`, to prevent your thread get dropped on floor, there are 3 strategy you can implement:
    CallerRunsPolicy: caller will have to handle the thread.
    Define a `RejectedExecutionHandler`

In the above `RejectedExecutionHandler`, we defined that if my thread get rejected by executor, I will call the queue, and use `put()` method (not offer()) to try add myself into the queue - the `put()` method is `blocking` method, it is not nice but probably simplest way to handle.