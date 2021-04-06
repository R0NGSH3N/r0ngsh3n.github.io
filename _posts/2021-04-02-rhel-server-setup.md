---
layout: post
title: X things to do after install Red Hat Enterprise Server
tags: Linux Red Hat Rhel 
categories: common
---

## Add your self into "sudoer"

"su" to root -> type "usermod -aG wheel your_id" then logout-login.

## Fix "Unable to read consumer identity Warning and Solution" problem

1. su -> edit file: /etc/yum/pluginconf.d/subscription-manager.conf -> change "enable=1" to "enable=0"
2. su -> edit file: /etc/yum/pluginconf.d/product-id.conf -> change "enable=1" to "enable=0"
3. clean the cache: rm -rfv /var/cache/yum/* -> yum clean all

## Fix Error: There are no enabled repositories in "/etc/yum.repos.d", "/etc/yum/repos.d", "/etc/distro.repos.d" error

1. sudo subscription-manager register
2. sudo subscription-manager refresh
3. sudo subscription-manager attach --auto

## Install & Set up Docker 

~~~bash
sudo yum install docker
docker run hello-world
~~~
