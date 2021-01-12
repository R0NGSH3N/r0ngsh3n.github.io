---
layout: post
title: Super Simple Spring Cloud Explained - Open Feign
tags: java Spring Cloud Microservice Feign client interface
categories: common
---

The feeling of Feign is very like JPA with `CrudRepository`: you defined the entity and `CrudRepository` generate all the CRUD methods for you, all you need just create `interface` to implement that.

Feign is very similar that way: on Consumer side, you define the interface and annotate to `wire` it with the producer service, then you can use that interface as local call. 

## Open Feign Client Setup

1. Dependency:

~~~gradle
	implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
~~~

2. Annotation on Spring boot Application class:

~~~java
@SpringBootApplication
@EnableFeignClients
public class HelloWorldConsumerApplication {

	@Bean
    @LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldConsumerApplication.class, args);
	}
}
~~~

3. Define interface and use `@FeignClient` annotation to wire it to producer
~~~java
			//this is the target serivce application name
@FeignClient("producer-helloworld-service")
public interface HelloWorldClient {
				//this is the path of target service	
    @GetMapping("/test/greeting")
    String getGreeting();
}
~~~

4. Implements
   
In your client application, use as following, first you wire the interface and then call the method from the interface:

~~~java
    private final HelloWorldClient helloWorldClient;

    public HelloWorldServiceConsumer(RestTemplate restTemplate, HelloWorldClient helloWorldClient){
        super();
        this.helloWorldClient = helloWorldClient;
    }

    @GetMapping("/withFeign")
    public String testHelloWorldServiceFeign(){
        String answer = this.helloWorldClient.getGreeting();
        return getAnswer(answer);
    }

    private String getAnswer(String answer){
        if("Hello World".equals(answer)){
            return "Successful called producer-helloworld-service";
        }else{
            return "Failed to call producer-helloworld-service";
        }
    }
~~~

## Conclusion

* Feign client is easier than restTemplate.
* You also can specify the url and ipaddress for those service that not register with Eureka server.
