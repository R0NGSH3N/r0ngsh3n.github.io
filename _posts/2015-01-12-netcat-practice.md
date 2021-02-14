---
layout: post
title: netcat practice
tags: Linux net cat
categories: common
---

`netcat` is the swiss army knife.

1. start server and client - test firewall

  start server

  ~~~bash
  nc -l -p 8080 
  nc -u -l -p 8080 # start udp server
  ~~~

  start client connect to server

  ~~~bash
  nc localhost 8080
  nc -u localhost 8080 # connect to udp server
  ~~~

2. File transfer

 start server

 ~~~bash
 nc -l -p 8080 > 1.txt
 ~~~

 start client to copy the file
 ~~~bash
 nc -N localhost 8080 < 1.txt
 ~~~

 `-N` means stop the connection after transfer

3. Scan port

  ~~~bash
  nc -z -v -n 127.0.0.1 21-25
  ~~~

4. Retriev banner infomation from port

~~~bash
nc -v 127.0.0.1 22
~~~

5. Remote shell:

  server side
  ~~~bash
  mkfifo /tmp/p # create pipe(fifo) file, purpose to collect comand from client side to /tmp/p file
  cat /tmp/p | /bin/bash 2>&1 |nc -l -p 8080 > /tmp/p 
  # cat read /tmp/p content and send to /bin/bah
  # bash run the command and pipe the result back to netcat (nc)
  # netcat send output to client
  ~~~

  client side

  ~~~bash
  nc -n 127.0.0.1 8080
  ~~~



