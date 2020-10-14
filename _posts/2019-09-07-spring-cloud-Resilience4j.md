---
layout: post
title: Super Simple Spring Cloud Explained - Resilience4j 
tags: java Spring Cloud Microservice circuit break latency Resilience4j
categories: common
---
Resilience4j is used to protect your microservices to have `cascading failure`, it deployed on the `consumer` side to protect `consumer` if `producer` has any failure.

Resilience4j actually has followng 4 parts:

1. Circuit Breaks: Cause Resilience4j is deployed at `consumer` side, so we need a way to determine the `producer` side service's status, and then based on the status (`OPEN`,`CLOSED`,`HALF_OPEN`) to config how we do. The official document is [here](https://resilience4j.readme.io/docs/circuitbreaker), which I think it is worth to read
2. Bulkhead: control how many concurrent threads. It has `Semaphore`(a.k.a object lock) or `ThreadPool` 2 ways to control
3. RateLimiter : control the interval time of each request to `producer` in nanosecond
4. Retry: control max time retry number.



## Using Resilience4j
You can use `decorate` way to use all the function, but I prefer use `Annotation`, which is much cleaner

1. Add Dependency

    In build.gradle, add following
~~~gradle
implementation 'io.github.resilience4j:resilience4j-spring-cloud2:1.1.0'
~~~

2. Update the `application.yml` 

~~~yaml
resilience4j.circuitbreaker:
  instances:
    producer-helloworld-service: #this is the name/label to be used later
      registerHealthIndicator: true
      ringBufferSizeInClosedState: 5
      ringBufferSizeInHalfOpenState: 3
      waitDurationInOpenState: 10s
      failureRateThreshold: 50
      recordExceptions:
        - org.springframework.web.client.HttpServerErrorException
        - java.io.IOException
        - java.util.concurrent.TimeoutException
        - org.springframework.web.client.ResourceAccessException
        - org.springframework.web.client.HttpClientErrorException
      ignoreExceptions:

resilience4j.ratelimiter:
  instances:
    producer-helloworld-service: #this is the name/label to be used later
      limitForPeriod: 5
      limitRefreshPeriod: 10000 #ms
      timeoutDuration: 1000ms

resilience4j.retry:
  instances:
    producer-helloworld-service: #this is the name/label to be used later
      maxRetryAttempts: 3
      waitDuration: 5000

resilience4j.bulkhead:
  instances:
    producer-helloworld-service: #this is the name/label to be used later
      max-concurrent-calls: 10
      maxWaitDuration: 10ms

~~~

as you can see we config the 4 parts.

3. Add `fallbackXXX` methods in `consumer`

4. Add annotation to the `consumer` 
~~~java
    @GetMapping("/withRestTemplate")
    @CircuitBreaker(name="producer-helloworld-service", fallbackMethod = "fallback")
    @Bulkhead(name = "producer-helloworld-service", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "fallbackBulkhead")
    @RateLimiter(name = "producer-helloworld-service", fallbackMethod = "fallbackForRatelimit")
    @Retry(name = "get", fallbackMethod = "fallbackRetry")
    public String testHelloWorldService(){
        String answer = this.restTemplate.getForObject("http://producer-helloworld-service/test/greeting", String.class);
        return getAnswer(answer);
    }
~~~

as you can see the `name` attribution in each annotation match the `application.yml`, and you need to create `fallback` methods to handle different situation.
