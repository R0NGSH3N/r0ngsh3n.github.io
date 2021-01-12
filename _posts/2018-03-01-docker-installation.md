---
layout: post
title: Install docker on Ubuntu
tags: Windows 10, WSL 2, Ubuntu, Docker CE 
categories: common
---

## Install Docker

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
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    ~~~

    now the problem here is you might not find docker has the corresponding version for your ubuntu-ish linux distro, so the `lsb_release -cs` might give "ulyssa"(mint release name) instead of "focal" (ubuntu release name), and docker doesn't have any release for "ulysssa". you need to check "https://download.docker.com/linux/ubuntu/dists/" where all the ubuntu release name avaiable, and then change the command like:

    ~~~bash
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
    ~~~

5. Refresh the dependencies

    ~~~bash
    sudo apt update
    ~~~

6. Install Docker-ce

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

## Install MySQL docker Image

1. pull mysql image from docer hub:

   ~~~bash
   sudo docker pull mysql 
   ~~~

2. verfiy the mysql docker image pulled

    ~~~bash
    docker images
    ~~~

3. Create persistent data storage and start the service in docker

    ~~~bash
    sudo mkdir /var/lib/mysql -p
    sudo docker run -d --name mysql-server -p 3306:3306 -v /var/lib/mysql:/var/lib/mysql -e "MYSQL_ROOT_PASSWORD=123" mysql
    ~~~

4. Use any mysql client to connect "127.0.0.1:3306" with password "123" to verify the server install successful.

## Load csv file into MySQL

1. Install pip3

    ~~~bash
    sudo apt update
    sudo apt install python3-pip
    export PATH="$HOME/.local/bin:$PATH"
    ~~~

2. Install `csvsql` and `PyMySQL`

    ~~~bash
    pip3 install csvkit
    pip3 install PyMySQL
    ~~~

3. Use `csvsql` tool to pipe in the csv file into mysql WITHOUT CREATING TABLE

    ~~~bash
   csvsql --db mysql+pymysql://root:123@localhost:3306/Test --tables HNPQCountry --insert HNPQCountry.csv
   ~~~

4. If you wanto control the data type, then import the data, you can use `csvsql` to generate table creation sql:

    ~~~bash
    csvsql --dialect mysql HNPQCountry.csv > /tmp/createTable.sql
    cat /tmp/createTable.sql
    CREATE TABLE `HNPQCountry` (
        `Country Code` VARCHAR(3) NOT NULL, 
        `Short Name` VARCHAR(30) NOT NULL, 
        `Table Name` VARCHAR(30) NOT NULL, 
        `Long Name` VARCHAR(73) NOT NULL, 
        `2-alpha code` VARCHAR(2), 
        `Currency Unit` VARCHAR(42) NOT NULL, 
        `Special Notes` VARCHAR(939), 
        `Region` VARCHAR(26) NOT NULL, 
        `Income Group` VARCHAR(19) NOT NULL, 
        `WB-2 code` VARCHAR(2), 
        `National accounts base year` VARCHAR(50), 
        `National accounts reference year` VARCHAR(9), 
        `SNA price valuation` VARCHAR(36), 
        `Lending category` VARCHAR(5), 
        `Other groups` VARCHAR(9), 
        `System of National Accounts` VARCHAR(61), 
        `Alternative conversion factor` VARCHAR(18), 
        `PPP survey year` BOOL, 
        `Balance of Payments Manual in use` VARCHAR(33), 
        `External debt Reporting status` VARCHAR(11), 
        `System of trade` VARCHAR(20), 
        `Government Accounting concept` VARCHAR(31), 
        `IMF data dissemination standard` VARCHAR(51), 
        `Latest population census` VARCHAR(166) NOT NULL, 
        `Latest household survey` VARCHAR(77), 
        `Source of most recent Income and expenditure data` VARCHAR(88), 
        `Vital registration complete` VARCHAR(48), 
        `Latest agricultural census` VARCHAR(130), 
        `Latest industrial data` DECIMAL(38, 0), 
        `Latest trade data` DECIMAL(38, 0), 
        CHECK (`PPP survey year` IN (0, 1))
    );
    ~~~
