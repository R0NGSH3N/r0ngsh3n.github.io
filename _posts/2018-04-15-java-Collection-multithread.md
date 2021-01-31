---
layout: post
title: Example for Spring Webflux WebClient
tags: Java Collection Multi thread
categories: common
---

There are 2 ways to run task async in a list:

1. use `parallel()`
2. use traditional `CompletableFuture`

 `parallel`'s threads limited to number of cores of CPU, if CPU is 8 cores, then every time starts 8 threads, no adjust no customized thread pool, this is same as 2nd way use without customized thread pool (executorService)

Example to show how to use `parallel` method:

~~~java
    public void parallelCalculator(List<Integer> list) {
        List<Integer> result = list.stream().parallel().map(e -> {
            System.out.println(e + " thread name: " + Thread.currentThread().getName());
            return e * 10;
        }).collect(Collectors.toList());

        result.forEach(System.out::println);
    }
~~~

to See hwo many core in your system:

~~~java
    System.out.println("how many core: " + Runtime.getRuntime().availableProcessors());
~~~

usually the `parallel` is good for `compute intense` task, but not for `IO intense` works, if you want to connect to other server do something then this is not good for your approach.

Use traditional `CompletableFuture` has 2 ways: with or without customized threadpool, if you don't use customized threadpool, your thread is from `ForkJoinPoo.commonPool()`, that is no different as using `parallel` method, but the benefit for 2 ways is to use `executorService`

First way to run (little bit complicated, but easy to understand)

~~~java
    public void asyncCalculator(List<Integer> list) {
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(list.size(), 1000));
        CompletableFuture<Integer>[] results = list.stream().map(e -> {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println(e + " current thread name: " + Thread.currentThread().getName());
                return e * 10;
            }, executorService);
        }).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(results).join();
        Arrays.asList(results).forEach(e -> {
            try {
                System.out.println(e.get());
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ExecutionException executionException) {
                executionException.printStackTrace();
            }
        });
        executorService.shutdown();
    }
~~~

Second way is much simpler, but not easy to understand, but general idea is first `Collections` is the `CompletableFuture<Integer>` list, and second one (after join) is the real result list

~~~java
    public void asyncCalculatorV2(List<Integer> list) {
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(list.size(), 1000));

        List<Integer> result = list.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            return e * 10;
        }, executorService)).collect(Collectors.toList()).stream().map(CompletableFuture::join).collect(Collectors.toList());

        executorService.shutdown();
        result.forEach(System.out::println);

    }
~~~