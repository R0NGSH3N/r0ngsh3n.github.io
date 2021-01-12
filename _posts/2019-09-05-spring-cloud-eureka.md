---
layout: post
title: Super Simple Spring Cloud Explained - Eureka
tags: java Spring Cloud Microservice Eureka Registry Discover
categories: common
---

Eureka is clean solution for Spring service registration and discovery. Eureka works very like Config server/client, it put all the `end-points` together instead of `config files`.

## Eureka Server

1. Create project from Spring Initializr
Don't forget add `Eureka Server` as dependency

2. Add annotation `@EnableEurekaServer`
~~~java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
~~~
3. Update `application.yml`

~~~yaml
server:
  port: 8761
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
~~~

## Eureka Client

1. Add Dependency in gradle:
```gradle
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
```

2. Update the `bootstrap.yml`

This is impoatant! you don't need to change `application.yml`, because we have moved the config file to the config server.

you also don't need `@EnableDiscoveryClient` or `@EnableEurekaClient`.

3. Rebuild the application, and start both Eureka server and you application (in here, I am still using the `producer-helloworld-server`)

4. go to `http://localhost:8761`, you should see your service listed:

![Picture 1](https://r0ngsh3n.github.io/static/img/0905/pic1.PNG)

## Conclusion

* Same shit as Config server/config client
* No coding required
