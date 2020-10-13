---
layout: post
title: Super Simple Spring Cloud Explained - Ribbon
tags: java Spring Cloud Microservice Ribbon Load Balancer 
categories: common
---

Ribbon is `load balancer` that work with Eureka, can provide the load balance on each service. 

Ribbon Client side setup

The Ribbon Configuration on the Client side is on the `RestTemplate` object/bean. Once you setup a `load balance` RestTemplate, then you can use any where as bean. you also can set up different RestTemplate implements different load balance strategies, and then use them for different purpose. below is `load balanced` RestTemplate in java config:

~~~java
	@Bean
    @LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
~~~

Once you define the RestTemplate, that is all you need. 

## Conclusion

* Same shit as Config server/config client
* No coding required
