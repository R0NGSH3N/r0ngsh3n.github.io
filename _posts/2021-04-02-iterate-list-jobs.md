---
layout: post
title: Iterate through list of jobs and run multithreads.
tags: Java Stream Multithreads
categories: common
---

Recently, I found a set of code that iterate through list of configs and start the job by using execution pool, and the way and style it use very different as I used to write, so I went through and write down this blog try to see what is the advantage of this sytle.

The class name is `SimpleSparkEtlProcessor`, it has following attributes and method:

1. list of config objects:

~~~java
    private ExecutorService executorService;
~~~

This list of config get initialized in the construction:

~~~java
    public SimpleSparkEtlProcessor(SimpleSparkEtlJobConfig config) {
        this.simpleSparkEtlJobConfig = config;
        this.lock = new ReentrantLock();
        this.running = new AtomicBoolean(true);
        this.extractConfigDirectoryList = config.getSourceConfigs();
    }
~~~

2. `run()` method, this is the start method.

~~~java
    public void run() {
        running.set(true);
//        Lock lock = locks.apply(simpleSparkEtlJobConfig);

        try {
            log.info("start watching folder... {}", Thread.currentThread().getName());

            while (running.get()) {
                if (acquireLock(lock)) {
                    List<Void> results = extractConfigDirectoryList.stream().map(sourceConfig -> CompletableFuture.runAsync(() -> {

                        Function<String, CompletableFuture<String>> sparkSubmitter = createSparkProcessor();
                        FileProcessor fileProcessor = new FileProcessor(sourceConfig, sparkSubmitter);
                        fileProcessor.process();

                    }, executorService)).map(CompletableFuture::join).collect(Collectors.toList());

                    lock.unlock();
                }
            }

        } finally {
            running.set(false);
            lock.unlock();
        }
    }

    public Boolean acquireLock(Lock lock) {
        try {
            return lock.tryLock();
        } catch (Exception e) {
            log.info("{}", e);
            return false;
        }
    }


~~~

- The whole code are wrapped in while loop - with an atomic boolean `running == true`

~~~java
while (running.get()) {
	...
}
~~~

- All the threads have to get lock of this object before going further, this to prevent another thread running on this Processor object.

~~~java
if (acquireLock(lock)) {
	...
}
~~~

- use stream to iterate `extractConfigDirectoryList` and foreach conig call `CompletableFuture.runAsync` to run the code **asychronize**

~~~java
extractConfigDirectoryList.stream().map(sourceConfig -> CompletableFuture.runAsync(() -> {
	...
}, executorService))
~~~

here it use the `CompletableFuture.runAsync(Runnable, ExecutorService)` interface, it return a `CompletableFuture<Void>` object.

- then use another `map` method of stream to run `CompletableFuture.join()` method that will block the current thread and once the all the job done, this will return `List<Void>` object since we call `runAsync` method.

- After all jobs done, unlock the lock, back to the while loop.

- Outside the while loop, there is `try...finally` to trap all the exception, if any thing happened, to prevent infinite loop, set the `running` to false and this will stop the while loop immediately

~~~java
try{
	...
} finally {
        running.set(false);
        lock.unlock();
}
~~~