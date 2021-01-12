---
layout: post
title: Angular Infrastructure Bottom Up 1
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

## Pipe

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

## Form

Form is used to collect data, there are 2 type of form in angular:

**Template driven Form**
**Model driven Form(AKA reactive Form)**

Model Driven Form proivde put the form in the component so you have more control, and this is what we want.

1. You need import the `ReactiveFormModule` in the `app.module.ts` file:

~~~javascript
import { ReactiveFormsModule } from '@angular/forms';
...

  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
~~~

2. Create form component by angular cli

~~~bash
ng g component person-form
~~~

3. In the `person-form.component.ts` file

~~~javascript
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-person-form',
  templateUrl: './person-form.component.html',
  styleUrls: ['./person-form.component.css']
})
export class PersonFormComponent implements OnInit {
  personForm : FormGroup;

  constructor() { }

  ngOnInit(): void {
    this.personForm = new FormGroup({
      id: new FormControl(''),
      name: new FormControl('', Validators.compose([
        Validators.required,
        Validators.pattern('[\\w\\-\\s\\/]+')
      ])),
      gender: new FormControl('', this.genderValidator)
    })
  }

  genderValidator(control: FormControl){
    if (control.value.trim().length === 0) {
      return null;
    }

    if(control.value === 'male' || control.value === 'female'){
      return null;
    }else{
      return { 'gender': true };
    }
  }

  onSubmit(person){
    console.log(person);
  }

}
~~~

`FormGroup` represents the entire form, so our form name is `personForm`, that is what we declare first, then in `ngOnInit` method, we create, and pass the 3 creation of `FormControl` to `FormGroup`'s constructor.

~~~javascript
  ngOnInit(): void {
    this.personForm = new FormGroup({
      id: new FormControl(''),
      name: new FormControl(''),
      gender: new FormControl('')
    })
  }
~~~

there are 2 `Validators` here, 1 is build-in, 1 is customized.

I use build-in `Validator` to validate if there is `illegal` charactor in `name`.

and use custom `Validator` to validate if the gender value either `male` or `female`. When build customize `Validator`, ***if you think the value is valid, then return `null`, otherwise return anything but null.***

one more thing, in the customize validator: `genderValidator`, we return 
~~~javascript
return { 'gender' : true };
~~~
if there is error, the `gender` String is can be capture in the `hasError('gender')` method and display the error message.

This is the benefit for creating `Model-driven` form, that creating forma and validating form are in ts file not in html file.

the `onSubmit` method is linked to `submit` button on the form.

4. Create Form in html file: `person-form.component.html`

~~~html
<header>
    <h2>Add New Person</h2>
</header>
<form [formGroup]="personForm" (ngSubmit)="onSubmit(personForm.value)">
    <ul>
        <li>
            <label for="id">Id</label>
            <input type="text" name="id" id="id" formControlName="id">
        </li>
        <li>
            <label for="name">Name</label>
            <input type="text" name="name" id="name" formControlName="name">
            <div *ngIf="personForm.get('name').hasError('pattern')">your name is not valid</div>
        </li>
        <li>
            <label for="gender">Gender</label>
            <input type="text" name="gender" id="gender" formControlName="gender">
            <div *ngIf="personForm.get('gender').hasError('gender')">You only can use male or female</div>
        </li>
    </ul>
    <button type="submit" [disabled] = "!personForm.valid">Save</button>
</form>
~~~

in the `<form>`, we add 2 directives: `formGroup` and `ngSubmit`, the `formGroup` directive match the formGroup define in ts file : `peronForm`. `ngSubmit` for submit form. the `name` attribute for `input` tag are matched in the ts file.

Notice here we have `disabled` directive used here, that means if the form is not valid (if there is any validator validate fail), then this button will "grey" out until all the field is valid.

In here, there is 2 "<div>" elements in `name` and `gender` inputs, that are used to display the error message, so the logic is, if the `personForm` - the `formGroup`  get `formControl` which here are `name` and `gender`, if their `Validator` functions (one use build-in, one use customize) `hasError`, then display those error message.

## Service as Dependent Injection

### Dependent Injection

Injectable Service could be injected through constructor:

we will use `FormBuilder` service to replace `FormGroup` in `person-form.component.ts` file. We inject that service via constructor:

~~~javascript
  constructor(private formBuilder: FormBuilder) { }
~~~

the `private` modifier notify typescript to create member variable `formBuilder`. We create service in the constructor because we will use it later in `ngOnit()` method, the constructor ran before `ngOnit()`, that is why we put the creating `FormBuilder` service here.

the Full update `person-form.component.ts` file here:

~~~javascript
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';

@Component({
  selector: 'app-person-form',
  templateUrl: './person-form.component.html',
  styleUrls: ['./person-form.component.css']
})
export class PersonFormComponent implements OnInit {
  personForm : FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.personForm = this.formBuilder.group({
      id: this.formBuilder.control(''),
      name: this.formBuilder.control('', Validators.compose([
        Validators.required,
        Validators.pattern('[\\w\\-\\s\\/]+')
      ])),
      gender: this.formBuilder.control('', this.genderValidator)
    })
  }

  genderValidator(control: FormControl){
    if (control.value.trim().length === 0) {
      return null;
    }

    if(control.value === 'male' || control.value === 'female'){
      return null;
    }else{
      return { 'gender': true };
    }
  }

  onSubmit(person){
    console.log(person);
  }

}
~~~

so in this case, the service provider `FormBuilder` injected in `PersonFormComponent` via its constructor.

### Service

1. Create new Service by angular CLI command:

~~~bash
ng g service person-list
~~~

2. Add the service in `app.module.ts`if you don't use `@Injectable`, I just list this step here, we are using `@Injectable` decorator which is preferred.

~~~javascript
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShowPersonComponent } from './show-person/show-person.component';
import { ShowPersonListComponent } from './show-person-list/show-person-list.component';
import { GenderDirective } from './gender.directive';
import { GenderpipePipe } from './genderpipe.pipe';
import { PersonFormComponent } from './person-form/person-form.component';
import { PersonListService } from './person-list.service';

@NgModule({
  declarations: [
    AppComponent,
    ShowPersonComponent,
    ShowPersonListComponent,
    GenderDirective,
    GenderpipePipe,
    PersonFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [
    PersonListService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
~~~

notice that `PersonListService` was added in `Providers`, not other array, cause this is service provider.

3. move the static `persons` object array from `show-persion-list.component.ts` to `person-list.service.ts` file, and then create the `get()`, `add()` and `delete()` method (CRUD processes). Following is the `person-list.service.ts` looks like:

~~~javascript
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PersonListService {

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

  get(){
    return this.persons;
  }

  add(value){
    this.persons.push(value);
  }

  delete(value){
    if(this.persons.indexOf(value) >= 0 ){
      this.persons.splice(this.persons.indexOf(value));
    }
  }
}
~~~

You must have to have `@Injectable` decorator in the service if you skip step 2. This is important.

4. Import `person-list.service.ts` into `show-person-list.component.ts`, and in `ngOnInit()` method, you can embeded the `get'` moethod

~~~javascript
import { Component, OnInit } from '@angular/core';
import { PersonListService } from '../person-list.service';

@Component({
  selector: 'app-show-person-list',
  templateUrl: './show-person-list.component.html',
  styleUrls: ['./show-person-list.component.css']
})
export class ShowPersonListComponent implements OnInit {
  persons; 

  constructor(private service: PersonListService){}


  ngOnInit(){
    this.persons = this.service.get();
  }

}
~~~

we remove the static `person'` array, and in the `ngOnInit` method, we assign the `person` array by service.


### alternative Service - Inject Token

Value provider usually provides list of global `consts` for look up, those values could be found through the project.
It is like 'Global Enum' but here it call ***Inject Token***

1. Create Inject Token file called `provider.ts`

~~~javascript
import { InjectionToken } from '@angular/core';

export const genderListToken = new InjectionToken('genderListToken');

export const genderList = {
    genders : ['male', 'female']
}
~~~

here, we have `InjectionToken` and our value list `genderList` both, we need to import them in the `app.moudule.ts` file.

2. Add in `app.module.ts`

~~~javascript

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShowPersonComponent } from './show-person/show-person.component';
import { ShowPersonListComponent } from './show-person-list/show-person-list.component';
import { GenderDirective } from './gender.directive';
import { GenderpipePipe } from './genderpipe.pipe';
import { PersonFormComponent } from './person-form/person-form.component';
import { genderListToken, genderList } from './provider';

@NgModule({
  declarations: [
    AppComponent,
    ShowPersonComponent,
    ShowPersonListComponent,
    GenderDirective,
    GenderpipePipe,
    PersonFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [ 
    { provide: genderListToken, useValue: genderList }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
~~~

after import, we add following line in the `providers` section:

~~~javascript
  { provide: genderListToken, useValue: genderList }
~~~

3. Use the Inject Token in `person-form.component.ts` file as `select` option.

~~~javascript
import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';
import { PersonListService } from '../person-list.service';
import { genderListToken } from '../provider';

@Component({
  selector: 'app-person-form',
  templateUrl: './person-form.component.html',
  styleUrls: ['./person-form.component.css']
})
export class PersonFormComponent implements OnInit {
  personForm : FormGroup;

  constructor(private formBuilder: FormBuilder, 
    private personlistService: PersonListService
    @Inject(genderListToken) public genderList ) { }

  ngOnInit(): void {
    this.personForm = this.formBuilder.group({
      id: this.formBuilder.control(''),
      name: this.formBuilder.control('', Validators.compose([
        Validators.required,
        Validators.pattern('[\\w\\-\\s\\/]+')
      ])),
      gender: this.formBuilder.control('', this.genderValidator)
    })
  }

  genderValidator(control: FormControl){
    if (control.value.trim().length === 0) {
      return null;
    }

    if(control.value === 'male' || control.value === 'female'){
      return null;
    }else{
      return { 'gender': true };
    }
  }

  onSubmit(person){
    this.personlistService.add(person);
    console.log(person);
  }

}
~~~

we import `Inject` from `angular/core` module, and import `genderListToken` from `provider.ts`, notice we don't need to import `genderList` from `provider.ts`, then in the `constructor` we create `Inject` public object `genderList`.

4. Modify the html file to use `dropdown` menu as gender selector

~~~html
<header>
    <h2>Add New Person</h2>
</header>
<form [formGroup]="personForm" (ngSubmit)="onSubmit(personForm.value)">
    <ul>
        <li>
            <label for="id">Id</label>
            <input type="text" name="id" id="id" formControlName="id">
        </li>
        <li>
            <label for="name">Name</label>
            <input type="text" name="name" id="name" formControlName="name">
            <div *ngIf="personForm.get('name').hasError('pattern')">your name is not valid</div>
        </li>
        <li>
            <label for="gender">Gender</label>
            <select name="gender" id="gender" formControlName="gender">
                <option *ngFor="let gender of genderList.genders" [value]="gender">{{gender}}</option>
            </select>
            <!-- <input type="text" name="gender" id="gender" formControlName="gender">
            <div *ngIf="personForm.get('gender').hasError('gender')">You only can use male or female</div> -->
        </li>
    </ul>
    <button type="submit" [disabled] = "!personForm.valid">Save</button>
</form>
~~~

we changed the `gender` section from `input` to `select`, and in the `option`, we use `for-loop` to loop through the `genderList.genders` array and show the values.