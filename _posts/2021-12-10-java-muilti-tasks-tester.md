---
layout: post
title: A simple method to test X number of tasks on Y numbers of service Threads
tags: Java
categories: common
---

## Requirement:

Assume our server have '200' execution threads, and we have '7000' concurrent request send in __at same time__, and by the end we need to verify how long those '7000' requests proceed.

## Java code as following:

~~~java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestCountDownLatch {
    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        CountDownLatch beforeProcessLatch = new CountDownLatch(7000);
        CountDownLatch afterAllCompleteLatch = new CountDownLatch(7000);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 7000; i++) {
            executorService.submit(() ->{
                try{
                    beforeProcessLatch.await();
                    TimeUnit.MILLISECONDS.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    afterAllCompleteLatch.countDown();
                }
            });
            beforeProcessLatch.countDown();
        }

        afterAllCompleteLatch.await();
        System.out.println("Completed, time consumed: " + ( System.currentTimeMillis() - startTime));
        System.exit(0);
    }
}
~~~

now we take step-by-step to see how this piece code works:

We have 2 `CountDownLatch":
   - beforeProcessLatch
   - afterAllCompleteLatch

the `beforeProcessLatch` is used to `line-up` all the threads to start at same time:

the for loop is 7000 times, simulate the 7000 requests, in that loop, we call `submit()` method of `ExecutorService` to start the thread, but each thread will NOT start immediately because they are all waiting for `beforeProcessLatch` to count down to 0.

When `beforeProcessLatch` count down to 0? __after the for loop__, because the count of `beforeProcessLatch` is same as for loop: __7000__, so after we jump out of the for loop, all the tasks start to run by 200 threads.

the `afterAllCompleteLatch` is used to `hold` main thread, the main thread have to wait for all the tasks completed so that we can come up a execution time. so each thread complete its work, in the `finally` block, it call `afterAllCompleteLatch` to minus 1, and the after all the `afterAllCompleteLatch` count down to 0, then main thread can start and print out the total execution time.