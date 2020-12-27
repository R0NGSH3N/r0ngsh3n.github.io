---
layout: post
title: Angular Infrastructure Botton Up 1
tags: Angular Typescript Javascript
categories: common
---

With Angular 10, it can create new project with command line `ng new` command, but I found that I lost the feeling about the infrastructure of Angular, so I try to dig in and try to learn Angular Infrastrcuture from bottom to up.

## Basic Components of Angular

### app.module.ts

~~~Javascript
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
~~~

This is Angular `root` module. the structure of this `app.module.ts` is no different as other component, but the `decorator` is different.

It is also the start point of Angular bootstrap. It bootstrap the `app.component.ts` as boot component.

The `imports` define all the required components that project need:

`BrowserModule` define this project is target for web browser platform.

you need to add `AppComponent` into `declarations` and `bootstrap` array to inform Angular that `app.component.ts` is the start component for this project.

### app.component.ts

This is `root` component, this will be straped by root module to `index.html` file.

~~~javascript
import { Component } from '@angular/core';
  
@Component({,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'hazelcast-server-ui';
}
~~~

The `selector` value will be use as `tag` in index.html.

### main.ts

the `app.module.ts` and `app.component.ts` are sitting in `src/app` folder, the `main.ts` is sitting in `src` folder, the `main.ts` bootstrap angular project to the `platform` -- that is my understanding, there are other `platform` angular support.

~~~javascript
import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
~~~

the `platformBrowserDynamic` defined that this project target browser platform.

the `main.ts` import the `platformBrowserDynamic` and then bootstrap the `AppModule` we defined in `app.module.ts` with it, the `platformBrowserDynamic` has all the functions designed for browser.

~~~javascript
platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
~~~

This line of code call bootstrap command.

With all 3 components above: `app.module.ts`, `app.component.ts` and `main.ts`, you angular app can be run as `ng s`.

## Angular template Syntax

### Interpolation {{ expression}}

  Not all the expression is allowed in the interpolation(like assignments), the major purpose of interpolation is for display data.

  You don't need 'this' or any prefix for showing the member variable in template.

  You also can call method from interpolation: {{ methodName() }}

### Property Binding

~~~html
<h [textContent] = "some_variable" ></h>
<h textContent = {{ some_variable }}></h>
~~~

This is 2 way binding, not like interpolation, Angular will monitor the DOM object and manage the data change.

### Event Binding

~~~html
<a (click)= "onClick()">click</a>
~~~

any javascript remove the `on` will be the event, there we remove the `onClick`'s `on` and that is angular `click` event, when someone click that link, the `onClick()` method from component.ts file will be triggered.

## Input/Output Decorators

According to Angular website, the Input/Output Decorator are ***Sharing data between child and parent directives and components***

My understanding is all the project component html (yourname.component.html) are the children of `app.component.html`, so they share same DOM object, the `@Input` and `@Output` decorators are the share components between all the components but you need to declare it.

### Input Decorator

eg. in `app.component.ts` (root component - also the parent component for all), you defined a object person.

~~~javascript
import { MaxLengthValidator } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  Person = {
    id: 1,
    name: 'r0ngsh3n',
    gender: 'male'
  }
}
~~~

now, if we have `show-person.component.ts` that show the `Person` object, we will pass the `Person` object from root component to this `show-person` component and display the information of `Person` object.

~~~javascript
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-show-person',
  templateUrl: './show-person.component.html',
  styleUrls: ['./show-person.component.css']
})
export class ShowPersonComponent {

  @Input() person;

}
~~~

3 things here:

- You need to import `Input` from angular/cole.
- you need declare variable `person` as `Input`.
- the `selector` define the `directive` name is `app-show-person`

then the `show-person.component.html` will take care the display `person` part.

here is the `show-person.component.html`

~~~html
<p>
    <li>id: {{ person.id}}</li>
    <li>name: {{ person.name}}</li>
    <li>gender: {{ person.gender}}</li>
</p>
~~~

1 last thing need to be taken care of, ***binding the input parameter from app.component.html to app-show-person directive***

here is the `app.component.html` page

~~~html
<div>
  <app-show-person [person] = "person"></app-show-person>
</div>
<router-outlet></router-outlet>
~~~

the binding (or more like pass-down) format is not different as `interpolation`:

~~~javascript
[person] = "person"
~~~

the `[person]` is the ***variable name*** in `show-person.component.ts` and `"person"` is the ***variable name*** from `app.component.ts`.

### Output Decorator

Output decorator is used to send data from children component to parent component (opposite of Input...no brainer).

But more important, ***the output decorator is usually partner with `EventEmitter` to send event from children to parent. In this case, the Output decorator is always defined as function.***.

## Structure Directive VS Attribute Directive

**Structure Directive:** used to change DOM layout by adding and removing DOM Element:

  `ngIf` and `ngFor`

**`nfIf`** Directive:

~~~html
<p>
  <li>id: {{ person.id}}</li>
  <li *ngIf="person.name">name: {{ person.name}}</li>
  <li>gender: {{ person.gender}}</li>
</p>
~~~

Those are most common structure directives in use. There are 2 format:

~~~html
<ng-template [ngIf]="person.name">
  <li>name: {{ person.name}}</li>
</ng-template>
~~~

~~~html
<p>
  <li>id: {{ person.id}}</li>
  <li *ngIf="person.name">name: {{ person.name}}</li>
  <li>gender: {{ person.gender}}</li>
</p>
~~~

above code check if the `person.name` is `null`, then it will NOT show the line of `name` attribute. `*ngIf` is synthetic sugar.

**`ngFor`** Directive:

`ngFor` directive is used to `for-loop`, so we create a list component to rendering `person`:

show-person-list.component.ts:

~~~javascript
import { Component } from '@angular/core';

@Component({
  selector: 'app-show-person-list',
  templateUrl: './show-person-list.component.html',
  styleUrls: ['./show-person-list.component.css']
})
export class ShowPersonListComponent {

  persons = [{
    id: 1,
    name: 'r0ngsh3n',
    gender: 'male'
  },
  {
    id: 2,
    name: 'johndoe',
    gender: 'male'
  },
  {
    id: 3,
    name: 'jeandoe',
    gender: 'female'
  }
  ]

}
~~~

In this compoent, we create list of `person` object, and in its html file, we wrap the `<app-show-person>` directive with `*ngFor` to provide the for loop of `persons` json list above:

~~~html
<div *ngFor="let person of persons">
    <app-show-person  (changeName)="onNameChange($event)" [person]="person">
    </app-show-person>
</div>
~~~

finally, we need to change `app.component.html` and replace `<app-show-person>` directive with our list direcive: `<app-show-person-list>`, from the structure view is like this:

app.component.html -> show-person-list.component.html -> show-person.compoent.html

## Attribute Directive

### Build in Directive

 `ngClass` - this is used to 'conditional' load CSS based on the object's attribute:

e.g: If we want to show 'female' person in red, we can change `show-person-list.component.html`

~~~html
<div *ngFor="let person of persons">
    <div [ngClass]="{ 'female-style' : person.gender === 'female', 'male-style': person.gender === 'male'}">
    <app-show-person  
        (changeName)="onNameChange($event)" 
        [person]="person">
    </app-show-person>
    </div>
</div>
~~~

The `[ngClass]` need "{}" and each condition separated by ",", and use `CSS Name : Object.Attribute === Value` format as expression.

and in `show-person.component.css` you can put any css file, you add the style:

~~~CSS
div.female-style {
    color: red
}

div.male-style {
    color: black
}

div.hovering-style {
    color: yellow
}
~~~

### Custom Directive

We can build a custom directive just like the build in directive:

The usage of custom directive is no different with build-in direcive, it follow the this format:

~~~javascript
  [appGender] = "person.gender"
~~~

the `appGender` is the `selector` and the `person.gender` is the `input` parameter pass from component to directive. Following show the step of creating directive.

1. Create directive ts file just like other component ts file:

~~~bash
ng g directive gender
~~~

This command will create following 2 files:
  `gender.directive.ts` and `gender.directive.spec.ts`.

2. In the `gender.directive.ts` file, we put code like following:

~~~javascript
import { Directive, HostBinding, Input } from '@angular/core';

@Directive({
  selector: '[appGender]'
})
export class GenderDirective {
  @HostBinding("class.female-style") isFemale = false;
  @Input() set appGender (value) {
    console.log('value is ' + value);
    if(value === 'female'){
      this.isFemale = true;
    }
  }
}
~~~

in here, the directive name is `appGender`, that is the name will be used in html, and ***The Input `set` to function name also MUST be `appGender`***, then this function will be called by angular when binding the directive to the host.

the `class.femail-style` is pointing to CSS class, the `class` here could be any HTML attribute (that is why called attribute directive), so when `person.gender` is `female`, in the host attribute, angular will add `class=female-style`.

3. Now add the custmized directive to the HTML page.

~~~HTML
<div *ngFor="let person of persons">
    <div [appGender] = "person.gender">
    <app-show-person  
        (changeName)="onNameChange($event)" 
        [person]="person">
    </app-show-person>
    </div>
</div>
~~~

4. Name Convention of Customize Directive

  In here, we just name `appGender` as directive name, in real world, you
should follow the angular directive name convention, just like all build-in directive, Angular name them with prefix `ng`, you should come up with your own prefix, so people know this is customized directive from your own application.

5. the customized directive not only can bind the attributes to host tag, it also can binding the `listener` to the host's event, like `onMouseEnter` event, following code shows how to handle `onMouseEnter` and `onMouseLeft` events

~~~javascript
import { Directive, HostBinding, Input, HostListener } from '@angular/core';

@Directive({
  selector: '[appGender]'
})
export class GenderDirective {
  @HostBinding("class.female-style") isFemale = false;
  @HostBinding("class.hovering-style") hover = false;

  @HostListener('mouseenter') onMouseEnter() {
    this.hover = true;
  }

  @HostListener('mouseleave') onMouseLeave() {
    this.hover = false;
  }

  @Input() set appGender(value) {
    console.log('value is ' + value);
    if (value === 'female') {
      this.isFemale = true;
    }
  }
}
~~~

here, we want the font color turn to 'yellow' when mouse hover, so we binding 2 listners on `onMouseEnter` and `onMouseLeave` event, in angular, all the HTML event are chopped of the `on`, so the events binding on `@HostListner` is `mouseenter` and `mouseleave` without `on`. we use boolean variable `hover` to control the CSS class `hovering-style`.

