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
