---
layout: post
title: Super Simple Spring Cloud - Externalized Configuration
tags: java Spring Cloud Microservice ConfigServer ConfigClient 
categories: common
---

External Config let you config all the service in 1 place, the benefit is not in the scope of this article.

official site: https://spring.io/projects/spring-cloud-config

There are 2 parts of Spring Cloud Config

1.`Config Server` - where all the configuration of the service at
2 `Config Client` - plug into each service that help service to retrieve configuration from server.

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



## Spring Config Server

Since we are going to put all the service's config files together in some where, so the name will not be `application.yml` any more, the file name will be `application name`.yml, eg: 

service name: producer-helloworld-service \
application config file at config server: producer-helloworld-service.yml




### Set up Git Repository as backend for config server
In this approach, The config files will be put in a **_separate_** git repo!, it could be put as `per repo per application(service)` or `per repo per profile`.

I prefer `per repo per profile`, that means I will have 4 git `repo`(per report per profile): `dev`,`qa`,`uat`,`prod`

The detail information: https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_server.html


### Set up File System as backend for config server
You also can put config into the local, so you don't need those git repo. but you need to start config server as `native`

1.Change your `application.yml` in config server like following:

~~~
server:
  port: 9001
spring:
  profiles:
    active: native
  cloud:
    config:
      server:
        native:
          searchLocations: classpath:config-repo/${profile}

management:
  security:
    enabled: false
~~~

2.copy config-repo to your `resources` folder

### How server find your yml file?

Once complete the config, start the server, then go to http://localhost:9001/producer-helloworld-service/dev

you can see the structure of restful path is different as the config file in file system. Depends on how you set up the config file structure, the query path will be different, following are some structures that Spring suggest.


here is the config files on my local file system:

![Picture 1](https://r0ngsh3n.github.io/static/img/0902/pic1.PNG)

```
/{application}/{profile}[/{label}] 
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```


`application`: is your service name, in my example, `application` = `producer-helloworld-service`

I define `profile` as environment: `dev`, `qa`,`uat`,`prod`

`label` usually is version, I don't have `label` here.

So, obviously, I am using the first structure which I think more most of sense. 

I will use `yml` as config file format.


## Spring Config Client