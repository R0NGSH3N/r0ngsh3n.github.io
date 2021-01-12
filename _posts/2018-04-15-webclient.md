---
layout: post
title: Example for Spring Webflux WebClient
tags: Spring java Webflux Weblient
categories: common
---

`WebClient` is asynchronous/non-blocking HTTP Client, it is replacement of `RestTemplate`, Here we walk through how to create and use `webClient`.

The Steps for `WebClient` to send request and receive response is following:

1. Create `WebClient` instance
2. Attach `post` or `get` method
3. Attach the `Header` section, usual `content-type` and/or `Authorization`.
4. Attach the `body` section
5. Attach the `retrieve` or `exchange` method if you want handle response
6. Attach `onStatus` to handle `404` or `500` error
7. Attach the `subscriber` to the `bodyToXXX` method, the webFlux is asynchronous method, so we need subsriber to handle the object asychronously.

## Add Dependency

~~~groovy
implementation 'org.springframework.boot:spring-boot-start-webflux'
~~~

## 1. Create Instance of WebClient

### 1.1 Three ways to create WebClient instance

~~~java
WebClient webClient = new WebClient.create();
~~~

or create with URL:

~~~java
WebClient webClient = new WebClient.create("http://127.0.0.1/some_link");
~~~

or with `builder` mode:

following define a content we will send is `json`
~~~java
WebClient webClient = WebClient.builder()
         .basheUrl("http://127.0.0.1/some_linke")
         .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
         .build();
~~~

in the `builder` mode, you can add `defaulHeader` and other information to the WebClient.

### 1.2 Insert `filter()` to log, modify the request and response

Inject `ExchangeFilterFunction` during the building to intercept the request, you can use this functio to log the request or modify them:

with lambda version:

~~~java
WebClient client = WebClient.builder()
        .filter((request, next) -> {

            logger.info("Request {} : {}", request.method(), request.url());

            return next.exchange(request);
        })
        .build();
~~~

with method:

~~~java
WebClient client = WebClient.builder()
        .filter(logRequest())
        .build();

private ExchangeFilterFunction logRequest(){
    return (request, next) -> {
            logger.info("Request {} : {}", request.method(), request.url());

            return next.exchange(request);
    };
}
~~~

If you want to modify the request, you need use `ClientRequest` class to extract the request:

~~~java
WebClient client = WebClient.builder()
        .filter((request, next) -> {

            ClientRequest filtered = ClientRequest.from(request)
                    .header("foo", "bar")
                    .build();

            return next.exchange(filtered);
        })
        .build();
~~~

the above example show the intercept the request and add attribution to the header.

### 1.3 Intecept the response

use `ofResponseProcessor()` factory pattern to create response filters:

~~~java
WebClient client = WebClient.builder()
        .filter( response -> {
            logger.info("Response status {}", response.statusCode());
            return mono.just(response);
        }
        .build();
~~~

## 2. Send Request by `get()` or `post()`

use `get()` or `post()` to use different HTTP methods.

## 3. Attach `header`

You can define `defaultHeader` when you create webClient, you also can attache the `defaultHeader` after `get()` or `post()` method by use `header()` method:

~~~java
webClient.get()
         .uri("/user/repos")
         .header("Authorization", "Basic " + Base64Utils
                  .encodeToString((username + ":" + token).getBytes(UTF_8)))
~~~

This insert a token in the header for authorization.

## 4. Attach `body`

### 4.1 Type of Request/Response Body

WebFlux is part of `Project Reactor`, it is non-blocking, asynchronous framework. Since it is Asychronous, so response back from Server could be 0, 1 till n. So Spring WebFlux provide following 2 type of object to wrap the response:

- `Mono<T>` which can be either return 0..1 response(s).
- `Flux<T>` which can be return 0..n response(s).

so if your service return 1 object, then use `Mono<T>`, if return `List`, then use `Flux<T>`

To send `Mono<T>` request body:

~~~java
webClient.post().uri("").body(Mono.just(OBJECT_INSTANCE_VARIABLE), YOUR_OBJECT.class)
~~~

put your object instance in the `just` method and determine the class of your object.

To receive `Mono<T>` response body:

~~~java
webClient.post().uri("").body(Mono.just(OBJECT_INSTANCE_VARIABLE), YOUR_OBJECT.class).retrieve().bodyToMono(YOUR_RESPONSE_OBJECT.class)
~~~

`retrieve()` is the method to handle response, and after retreive, we use `bodyToMono` method 

### 4.2 Three ways to define `body`

a. use `.body()`

~~~java
webClient.post().uri("").body(Mono.just(OBJECT_INSTANCE_VARIABLE), YOUR_OBJECT.class)
~~~

This is normal way.

b. use `syncBody()` (Deprecated)

This is shortcut version of `body()`, if you just have 1 object to send, you can use following format:

~~~java
webClient.post().uri("").syncBody(OBJECT_INSTANCE_VARIABLE)
~~~

c. use `BodyInserteres` class to create `BodyInserter` and then pass it in `body()`, this is for those `FormData`, `MultipartData` (file upload) those special.

## 5. Handle Response by `retrieve()` or `exchange()`

After you send the request to server, you need to see the response, there are 2 ways to handle: `retrieve()` is simpler, if you want to more control on response, then you can use `exchange()` method. `exchange()` provide full access to repsonse's headers and body.