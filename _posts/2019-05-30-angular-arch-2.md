---
layout: post
title: Angular Infrastructure Bottom Up 2
tags: Angular Typescript Javascript
categories: common
---

## 6. Pipe

Pipe used to transform or format the value, the format is:

"{{ value | pipe_name: input paramenter1: input paramter 2 | pipe_name2}}"

eg we want to format the name and slice only show 5 charactors and upcase:

~~~html
<p>
    <li>id: {{ person.id}}</li>
    <li *ngIf="person.name">name: {{ person.name | slice: 0:4 | uppercase}}</li>
    <li>gender: {{ person.gender}}</li>
</p>
~~~

you also can customize the pipe, say that we want to create `<header>` to list all the `genders` for person object (we have 2: male/female). we can use `pipe` to easy achive this.

1. generate pipe by using angular cli

~~~bash
ng g pipe genderpipe
~~~

2. in the `genderpipe.pipe.ts` file:

~~~javascript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'genderpipe'
})
export class GenderpipePipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return null;
  }

}
~~~

the `@Pipe` decorator has 2 inputs: `name` and `pure`, `pure` define if the pipe is stateful or stateless, most of time, `pipe` is stateless, and it is default so could be ignore.

the `pipe` need to implements `PipeTransform` interface and use `transform` method, the first parameter is the input value, and rest of parameter(s) are the paramters. We need person list as input value, and we don't need parameters to format the value.

We need to find the **unique** gender for each `person` object and return string like `male, female`, so we loop the list and find unique gender 

~~~javascript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'genderpipe'
})
export class GenderpipePipe implements PipeTransform {

  transform(persons, ...args: unknown[]): string{
    const genders = [];
    persons.forEach( element => {
        if(genders.indexOf(element.gender) <= -1){
          genders.push(element.gender);
        }
    });

    return genders.join(', ');
  }
}
~~~

3. add the `genderpipe` into the list html page:

~~~html
<header>
    <div>{{ persons | genderpipe }}</div>
</header>
<div *ngFor="let person of persons">
    <div [appGender] = "person.gender">
    <app-show-person  
        (changeName)="onNameChange($event)" 
        [person]="person">
    </app-show-person>
    </div>
</div>
~~~
