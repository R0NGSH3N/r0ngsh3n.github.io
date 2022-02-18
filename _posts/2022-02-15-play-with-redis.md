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

### Redis basic commands

#### show system info

in `./redis-cli` shell:

~~~bash
info
info [keyword]  keyword could be: keyspace, memory, CPU etc
~~~

but you also can use command line:

~~~bash
./redis-cli INFO
~~~

#### show all the available databases

~~~bash
127.0.0.1:6379> config get databases
1) "databases"
2) "16"
127.0.0.1:6379> 
~~~

This showed that redis has 16 databases available in redis

#### change database

by default, it point at "0" database (index), but you can change the database by using `select` command:

~~~bash
127.0.0.1:6379> select 10
OK
127.0.0.1:6379[10]> 
~~~

This change database to 10

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

## SpringBoot + Jedis + Redis

Jedis can be integrated into SpringBoot project just 3 steps:

1. Define the yaml/propertie of Jedis
2. Define the config java class of Jedis
3. 
## Redis Data type

source: https://redis.io/topics/data-types-intro

Redis Commands (Chinese version): http://redisdoc.com/
