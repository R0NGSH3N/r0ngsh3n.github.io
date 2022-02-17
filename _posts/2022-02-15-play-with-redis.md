---
layout: post
title: Redis for Beginner
tags: Java
categories: common
---

## Redis Documents

 Github: https://github.com/redis/redis

 Redis Documentation: https://redis.io/documentation

## Download Redis

~~~bash
git clone https://github.com/redis/redis.git
~~~

## Build Redis

Simple as `make` under the redis root folder

## Run Redis

after build:

~~~bash
cd redis/src
./redis-server
~~~

## Install Redis

Install Redis into `/usr/local/bin` environment

~~~bash
cd utils
./install_server.sh
~~~

## Redis CLI

Redis doesn't have web ui, you need use command line tools to connect to redis (usually localhost:6379)

~~~bash
cd redis/src
./redis-cli
127.0.0.1:6379> set name r0ngsh3n
OK
127.0.0.1:6379> get name 
"r0ngsh3n"
~~~

## Jedis + Redis

in build.gradle, add `Jedis` dependency:

~~~groovy
    implementation 'redis.clients:jedis:4.1.0'
~~~

then write your code:

~~~java
   public static void main(String[] args){
        JedisPooled jedis = new JedisPooled("localhost", 6379);
        jedis.set("your_name", "hello_world");
        String value = jedis.get("your_name");

    }
~~~