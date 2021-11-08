---
layout: post
title: Setup SSH Server on Unbuntu
tags: Linux Ubuntu SSH server
categories: common
---

## Setup ssh Server on Unbuntu 20.10

1. Install `openssh-server`

    ~~~bash
    sudo apt update
    sudo apt install openssh-server
    ~~~

2. verify the service is up

    ~~~bash
    sudo systemctl status ssh

    [sudo] password for: 
    ● ssh.service - OpenBSD Secure Shell server
         Loaded: loaded (/lib/systemd/system/ssh.service; enabled; vendor preset: enabled)
         Active: active (running) since Thu 2021-02-25 09:40:42 PST; 1h 19min ago
           Docs: man:sshd(8)
                 man:sshd_config(5)
       Main PID: 2890 (sshd)
          Tasks: 1 (limit: 9446)
         Memory: 1.3M
         CGroup: /system.slice/ssh.service
                 └─2890 sshd: /usr/sbin/sshd -D [listener] 0 of 10-100 startups

    Feb 25 09:40:42 ubuntu systemd[1]: Starting OpenBSD Secure Shell server...
    Feb 25 09:40:42 ubuntu sshd[2890]: Server listening on 0.0.0.0 port 22.
    Feb 25 09:40:42 ubuntu sshd[2890]: Server listening on :: port 22.
    Feb 25 09:40:42 ubuntu systemd[1]: Started OpenBSD Secure Shell server.

    ~~~

3. Turn on the firewall

    ~~~bash
    rongshen@ubuntu:/tmp/r0ngsh3n-vim$ sudo ufw allow ssh
    Rules updated
    Rules updated (v6)
    ~~~

4. Stop and restart the SSH server

    Stop
    ~~~bash
    sudo systemctl stop ssh
    ~~~

    Start
    ~~~bash
    sudo systemctl start ssh
    ~~~

    enable and disable
    ~~~bash
    sudo systemctl enable/disable ssh
    ~~~
