---
layout: post
title: Angular Infrastructure Bottom Up - HTTP with Mock backend Server 
tags: Angular Typescript Javascript
categories: common
---

Angular Buildin HTTP is used to connect to backend for data, it used couple of interface so the structure looks weird (to me), so I want to 'destructure' the HTTP Client to see what are in the design.

## Import HTTP module into `app.module.ts`

~~~javascript
...
import { HttpClientModule } from '@angular/common/http';
...
imports: [
  BrowserModule,
  AppRoutingModule,
  ReactiveFormsModule,
  HttpClientModule
],
~~~

## Create Backend Mock server

we don't have backend HTTP server, to test out the HTTP object in Angular, we can use the Mock class to simulate the backend.

## using HTTP client in service

http client are used in service for data exchange.

1. import `HttpClient`

~~~javascript
import { HttpClient } from '@angular/common/http';
~~~

2. Inject through constructor

~~~javascript
constructor(private http: HttpClient){}
~~~

3. Create `get` method

Let's simple start with:

~~~javascript
get() : Observable<person[]>{
  return this.http.get<person[]>("persons");
}
~~~

the `http.get()` method will return a `Observable` object of `person` list. We never created `person`  object before, so this line will give us compilor error, what need to do is add interface of `person` like following:

~~~javascript
interface person{
  id: number;
  name: string;
  gender: string;
}
~~~

So far this is okay to use, but I need to dig in, `http.get()` actually return `HTTPResponse` object, we use `<person[]>` to tell angular how to map the `HttpResponse` object to the our `person` object. It is turn out that you can use `map()` method from `Rxjs` to implemnt this:

~~~javascript
  get() : Observable<person[]>{
    return this.http.get<personResponse>(this.url).pipe(
      map((response: personResponse) => {
        return response.persons;
      })
    );
  }

  ...

interface person{
  id: number;
  name: string;
  gender: string;
}

interface personResponse{
  persons: person[];
}
~~~

here, we first use `pipe` method get the `HttpResponse` object, then use `map` method to unwrap the `HttpResponse` object and get the `person` object out of the response object. The `personResponse` object we define here is bridge that can provide more flexibilty on mapping...

`pipe` as its name imply, it take a `Observable` and give to function -- the parameter of `pipe` method -- then pass `Observable` out, and that is it. With this features of `pipe`, we can pass function to do all kind of thing to the `Observable` lke mapping, catch the error etc.

Here we pass `map` function to pipe to strip off the `HttpResponse` and return the the result we want.

4. Add `subscribe` to service client

In the `show-person-list.component.ts` file, we current have call service with following code:

~~~javascript
  ngOnInit(){
    this.persons = this.service.get();
  }
~~~

but the `http client`'s `get` method will not execute until someone `subscribe` to it, so we need to change the way to call and also what we got back is `Observable`, so we need to strip off the `Observable`.

~~~javascript
  ngOnInit(){
    this.service.get().subscribe(
      persons => {
        this.persons = persons;
      });
  }
~~~

In here, we implement the `subsribe` method and in the method we pass the `persons` array to our `this.persons`.