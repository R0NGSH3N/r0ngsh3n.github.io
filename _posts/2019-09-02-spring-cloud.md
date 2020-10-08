---
layout: post
title: Super Simple Spring Cloud Explained(Hoxton version) 
tags: java Spring Cloud Microservice 
categories: common
---

## Overview

## Create Spring Cloud project : How to create Spring Cloud Project?

You can either create project by `Spring Initializr`: by adding cloud components from `Dependencies` button or create Spring boot project first, and then add clound components. I prefered the second method, because in first approach, you have so many components in dependencies, as a beginner, how you know which you need to download?

<u>So Our approach is download the spring boot project then build from there:</u>

## Add Spring Cloud into your Spring boot project
Assume you have created spring boot project, now update the `build.gradle` as following to add Spring Cloud function to your Spring Boot project.

~~~gradle
plugins {
	id 'org.springframework.boot' version '2.3.4.RELEASE'
	id 'io.spring.dependency-management' version '1.0.10.RELEASE'
	id 'java'
}

group = 'com.r0ngsh3n.restful'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'com.h2database:h2'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
}

test {
	useJUnitPlatform()
}
~~~

<Strong>You MUST need to understand the different Spring Cloud version first, I am using `Hoston` version</Strong>

| Release Train | Boot Version |
|---------------|--------------|
| Hoxton        | 2.2.x and up |
| Finchley      | 2.0.x        |

I list 2 version I think most user friendly here. Please notice Spring Cloud Release naming is very different from other Spring projects.

## Service Producer and Consumer
There are 2 roles in Microservices Archtechture: 
Consumer : consume service. eg: query a `@Service` to get user object
Producer:  provide service. eg: `UserService` provide `findByUserName` service(method)

## Components

| Component name | Function Description             |
|----------------|----------------------------------|
| Spring Actuator| Monitor your application's health|
| Eureka/Zookeeper | Service Registration provider(for Service Discovery)  |
| Ribbon         | Load Balancer (for service)|
| Feign (Client) | replacement of RestTemplate/Web Client|
| Hystrix|isolate failed service |
| Spring Cloud Gateway | Microservice Gateway|
| Spring Cloud Config | Server Config/Client Config|
| Spring Cloud Slueth | Provide (Services) Distributed Tracing|


### Spring Actuator and Micrometer
**In Spring Boot 2.x Actuator disabled most of `endpoint`, only `/health` and `/info` are available

### Zookeeper
I use Zookeeper here, it is better than Eureka, here is [why.](https://medium.com/knerd/eureka-why-you-shouldnt-use-zookeeper-for-service-discovery-4932c5c7e764)

### Ribbon

### Feign

### Hystrix

### Spring Cloue Gateway (say no to Zuul)
### Spring Cloud Config
### Spring Cloud Sleuth
### Spring Cloud Stream

## Conclusion

* Validation should leave to Model (User object in this case) object.
* There should be a center point to handle all the exception cross the board.
* The Response object should be an unified format.
* Controller Advice is solution for handle request/response cross the board, with this function, we can handle Exception and Response cross the board.
