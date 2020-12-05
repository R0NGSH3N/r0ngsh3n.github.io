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

1.Update and Upgrade

~~~bash
sudo apt update && sudo apt upgrade
~~~

2.Install NodeJS

~~~Bash
sudo apt install nodejs
nodejs -v
~~~

3.Install npm:

~~~Bash
sudo apt install npm
npm -v
~~~

4.Install Angular Cli

~~~Bash
sudo npm install -g @angular/cli
ng version
~~~

5.Create new Angular project

**Where to create Angular project in spring boot?**

Spring boots load web application from following default directory:

if you pack your angular project as jar, then:

`/static`,

`/public`,

`/resources`,

`/META-INF/resources`

if you don't pack the angular project, Spring boot will run the angular application from

`src/main/webapp`

What we are saying here is:

1. run the Angular project with spring boot in `src` folder, this you need to spring boot look up in `src/main/webapp`, this method you don't need gradle, it is good for coding phase.

2. Integrate Angular project with spring boot build. you need add angular build method in gradle, so when you `jar` or `bootJar` your spring boot project, _you can have angular project package with the spring boot project_.


Then in this case, you need to use IDE's `run as java applicaion' on SpringBoot Application class, then you can drop your angular files over here, otherwise your angular files will be ignored.

**Basically, you can put your angular project any where, but you need to change the `outputPath` setting in `angular.json` file to let the complied code output to `src/main/webapp` folder**

eg, I build my angular project in `src/main/static/my-app` folder, then in the `angular.json` file, you need to change the the `outputPath` as following:

~~~json
          "builder": "@angular-devkit/build-angular:browser",
          "options": {
            "outputPath": "../../../webapp/",
            "index": "src/index.html",
            "main": "src/main.ts",
            "polyfills": "src/polyfills.ts",
            "tsConfig": "tsconfig.app.json",
            "aot": true,
~~~

since my angular project is in `src/main/static/my-app`, so the `webapp` folder is `../../../`. after you run `ng b`, the compiled code will be dropped in `src/main/webapp`, you start your application, then the your angular website will show as `localhost:8080` which is spring boot http listenning port ( not 42000 the default angular nodejs server port).

~~~bash
/src/main/resources/static
~~~

run the create angular command:

~~~bash
ng new my-app
~~~

After the command run, the full structure of Angular folder will be like this:

![Picture 1](https://r0ngsh3n.github.io/static/img/0411/angular-project-structure.png)

After click 2 `y`, 1 for strict mode, 1 for add angular router, and choose the CSS/SASS/LESS style then you should find `my-app` directory created under the `static` folder.

