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


## 7. Form

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

In here, there is 2 "div" elements in `name` and `gender` inputs, that are used to display the error message, so the logic is, if the `personForm` - the `formGroup`  get `formControl` which here are `name` and `gender`, if their `Validator` functions (one use build-in, one use customize) `hasError`, then display those error message.
