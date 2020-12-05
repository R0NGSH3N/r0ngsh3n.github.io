---
layout: post
title: 2020 Angular for Spring Boot step-by-step
tags: Angular NodeJs Spring boot TypeScript
categories: common
---

Install Angular with Spring boot 2020.

***
Environment:

- OS: Linux OS 20.04
- NodeJS: v10.19.0
- NPM: 6.14.4 
- Angular: 

***

1. Update and Upgrade

~~~bash
sudo apt update && sudo apt upgrade
~~~

2. Install NodeJS

~~~Bash
sudo apt install nodejs
nodejs -v
~~~

3. Install npm:

~~~Bash
sudo apt install npm
npm -v
~~~

4. Install Angular Cli

~~~Bash
sudo npm install -g @angular/cli
ng version
~~~

5. Create new Angular project

Spring boots load web application from following default directory:

if you pack your angular project as jar, then:
`/static`,

`/public`,

`/resources`,

`/META-INF/resources`

if you don't pack the angular project, then you can leave in :

`src/main/webapp`

 then you need to use IDE's `run as java applicaion' on SpringBoot Application class, then you can drop your angular files over here, otherwise your angular files will be ignored.

but because I will run the spring boot through "bootJar" task, I will put the angular into the `/resources/static/` folder

~~~bash
/src/main/resources/static
~~~

run the create angular command:

~~~bash
ng new my-app
~~~

After click 2 `y`, 1 for strict mode, 1 for add angular router, and choose the CSS/SASS/LESS style then you should find `my-app` directory created under the `static` folder.

