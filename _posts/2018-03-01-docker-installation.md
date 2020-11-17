---
layout: post
title: Install docker on Ubuntu
tags: Windows 10, WSL 2, Ubuntu, Docker CE 
categories: common
---
Official Guide: https://docs.docker.com/engine/install/ubuntu/


Following are the steps(assume you have ubuntu installed on WSL):

1. Update and Upgrade
~~~bash
sudo apt update && sudo apt upgrade
~~~
2. Install Dependecies
~~~bash
 sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common
~~~
3. Tell the System that all the software provided by Docker is trustable(downlad the GPG key and add to system).
~~~bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
~~~
You should see "OK" as response.
4. Add Docker Repository (docker source)
~~~bash
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
~~~
5. Refresh the dependencies
~~~bash
sudo apt update
~~~
6. Install Docker cE
~~~bash
sudo apt-get install docker-ce docker-ce-cli containerd.io
~~~
7. Manage Docker as non-root user
~~~bash
sudo groupadd docker
sudo usermod -aG docker $USER
~~~
8. Log out and log back
9. Check the Docker installation with hello-world
~~~bash
docker run hello-world
~~~