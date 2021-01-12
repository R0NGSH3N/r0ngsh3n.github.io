---
layout: post
title: Angular Infrastructure Bottom Up 2
tags: Angular Typescript Javascript
categories: common
---


## 8. Service as Dependent Injection

### 8.1. Dependent Injection

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

### 8.2. Service

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


### 8.3. alternative Service - Inject Token

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