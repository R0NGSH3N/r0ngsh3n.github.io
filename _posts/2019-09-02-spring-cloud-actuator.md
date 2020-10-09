---
layout: post
title: Super Simple Spring Cloud - Actuator
tags: java Spring Cloud Microservice Actuator
categories: common
---

Acuator is tools that can be used to monitoring your application's health status. The official guide is here:

https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready

## Spring Cloud family: 

| Component name | Function Description             |
|----------------|----------------------------------|
| Spring Actuator| Monitor your application's health|
| Eureka/Zookeeper | Service Registration provider(for Service Discovery)  |
| Ribbon         | Load Balancer (for service)|
| Feign (Client) | replacement of RestTemplate/Web Client|
| Hystrix|isolate failed service |
| Spring Cloud Gateway | Microservice Gateway|
| Spring Cloud Config | Externalized Config|
| Spring Cloud Slueth | Provide (Services) Distributed Tracing|


### Spring Actuator 
Once you added the actuator package in `dependencies` either in Maven or Gradle, your application is actuator enabled. The link below will show once your application started:
`http://localhost:8079/actuator/health`, it will tell you `status = up`...

The `health` is called `technology-agnostic endpoints`, the full list is [here](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints)

**In Spring Boot 1.x Actuator disabled most of `endpoint`, only `/health` and `/info` are available.

to enable all the endpoints, you need add following line `application.yml`, it enable all the endpoints.
```yaml
management:
  endpoints:
    web:
      exposure:
        # open all the monitor points
        include: '*'
  endpoint:
    health:
      # display all the health indicators
      show-details: always

```
But I think following `endpoints` is most useful:
`health` -- basic information about the service. the Default `health` only provide status = ok those non-sense message, if you want more detail message, add following in `application.yml`, under the `management` section
```yaml
  endpoint:
    health:
      # display all the health indicators
      show-details: always
```
`env` -- information about OS, JVM and etc.
`mappings` -- show all the `@RequestMappging` path
`threaddump` -- show thread dump of the JVM, quick diagnose if you can't log on production machine.

`httptrace` -- in Spring boots 1.2.x and up, this function need to **manually** add an `InMemoryHttpTraceRepository` in `@Configuration` class:
```java
    @Bean
    public HttpTraceRepository htttpTraceRepository(){
        return new InMemoryHttpTraceRepository();
    }

```

`metrics` and `metrics/{metrics name}` -- **_metrics is very useful endpoints_** but it take 1 steps:
0.Get metrics list: http://localhost:8080/actuator/metrics
```json
{
  "names": [
    "hikaricp.connections",
    "hikaricp.connections.acquire",
    "hikaricp.connections.active",
    "hikaricp.connections.creation",
    "hikaricp.connections.idle",
    "hikaricp.connections.max",
    "hikaricp.connections.min",
    "hikaricp.connections.pending",
    "hikaricp.connections.timeout",
    "hikaricp.connections.usage",
    "http.server.requests",
    "jdbc.connections.active",
    "jdbc.connections.idle",
    "jdbc.connections.max",
    "jdbc.connections.min",
    "jvm.buffer.count",
    "jvm.buffer.memory.used",
    "jvm.buffer.total.capacity",
    "jvm.classes.loaded",
    "jvm.classes.unloaded",
    "jvm.gc.live.data.size",
    "jvm.gc.max.data.size",
    "jvm.gc.memory.allocated",
    "jvm.gc.memory.promoted",
    "jvm.gc.pause",
    "jvm.memory.committed",
    "jvm.memory.max",
    "jvm.memory.used",
    "jvm.threads.daemon",
    "jvm.threads.live",
    "jvm.threads.peak",
    "jvm.threads.states",
    "logback.events",
    "process.cpu.usage",
    "process.start.time",
    "process.uptime",
    "system.cpu.count",
    "system.cpu.usage",
    "tomcat.sessions.active.current",
    "tomcat.sessions.active.max",
    "tomcat.sessions.alive.max",
    "tomcat.sessions.created",
    "tomcat.sessions.expired",
    "tomcat.sessions.rejected"
  ]
}
``` 
1.use the item in this list eg `jvm.memory.used` to see current JVM memory usage:

```json
{
  "name": "jvm.memory.used",
  "description": "The amount of used memory",
  "baseUnit": "bytes",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 138291399
    }
  ],
  "availableTags": [
    {
      "tag": "area",
      "values": [
        "heap",
        "nonheap"
      ]
    },
    {
      "tag": "id",
      "values": [
        "CodeHeap 'profiled nmethods'",
        "G0 Old Gen",
        "CodeHeap 'non-profiled nmethods'",
        "G0 Survivor Space",
        "Compressed Class Space",
        "Metaspace",
        "G0 Eden Space",
        "CodeHeap 'non-nmethods'"
      ]
    }
  ]
}
```

finally, this is the full content of my application.yml:
```yaml
server:
  port: 8079
spring:
  jpa:
    # pring SQL in hibernate
    show-sql: true
logging:
  level:
    root: INFO
    # hibernate logging setting
    org.hibernate: INFO
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.hibernate.type.descriptor.sql.BasicExtractor: TRACE
management:
  endpoints:
    web:
      exposure:
        # open all the monitor points
        include: '*'
  endpoint:
    health:
      # display all the health indicators
      show-details: always
```
