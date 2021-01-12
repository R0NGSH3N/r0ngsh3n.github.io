---
layout: post
title: Microservice in Intellij with Spring Cloud and Github 
tags: java Spring Cloud Microservice Intellij Github
categories: common
---

This Article show how to set up a `Micro Service` projects in Intellij IDEA community version, But you don't need to repeat this again, you can 


[```git clone https://github.com/R0NGSH3N/spring-cloud-template.git```](https://github.com/R0NGSH3N/spring-cloud-template)

In intellij, open the `build.gradle` file as project, and you will get "skeleton" spring cloud project in system.

## Trouble
When I start to learn micro services with Spring cloud, the first question for me is what is the project structure for micro service? because there are so many different service in a big umberella and there is no clean way to set up. So I try to write down my approach howt to set up those projects and hope there is easier way.

The spring cloud micro service project are consist of `parent` and `children`. The `parent` is nothing but a folder, and each `children` represents a spring-boot application as service.

Following is snapshot of my spring cloud project structure looks like in Intellij IDEA:

![Picture 10](https://r0ngsh3n.github.io/static/img/0923/intellij-tips-10.PNG)

## Steps:

1.Create project in github as start:
![Picture 1](https://r0ngsh3n.github.io/static/img/0912/pic1.PNG)

2.Open Intellij go `File` -> `New` -> `Project from Version Control...`, in the following dialog, input the `clone` 
![Picture 2](https://r0ngsh3n.github.io/static/img/0912/pic2.PNG)

3.Goto `File` -> `New` -> `Module`, After select jdk and maven/gradle, in 2nd screen, make sure the `Location` is under your umberella project and `parent` is None.
![Picture 3](https://r0ngsh3n.github.io/static/img/0912/pic3.PNG)

4.Once you created new module, you should see the sub-directory created in project. Open the `build.gradle` file in the new sub-directory root, and add dependencies:

* I use gradle, so for the maven user, just need to update `pom.xml` file

~~~ruby
plugins {
    id 'org.springframework.boot' version '2.3.4.RELEASE'
    id 'io.spring.dependency-management' version '1.0.10.RELEASE'
    id 'java'
}

group 'org.r0ngsh3n.springcloud.service'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.h2database:h2'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
}
~~~

5.rebuild the project. 



