---
layout: post
title: Intellij Live Templates and Postfix 
tags: java IDE Intellij Productivity 
categories: common
---
## Useful Live template for Java

### In Class

`main` and `psvm` - generate main method

### In Method:

`sout` - System.out.println

`soutp` - System out print input parameters

`soutv` - System.out.print closest variable name and value

`ifn` and `inn` if null statement and if not null

`lazy` if closest variable is null, instantiate it

`prsf` and `psf` - define constants : private(public) static final

`psfi` and `psfs` - define constants String/int : public static final

`thr` - throw new exception

`B` and `P` - surround with {} or ()

### Iteration

`fori` and `itar` - empty for loop with "i" / cloest array iteration

`itco` - for loop with "iterator" on collection object(List, Map etc)

`iter` - new format of for loop with iterator: for(Object element: collection){...}

## Useful Postfix Completion for Java

`Number.fori` - create for loop while i < `Number`

`Variable.arg` - This will put `Variable` as parameter into a method

`Boolean_Variable.if` - this create if(`Boolean_Variable`) statement

`Array_Variable.iter` - this will create for loop on `Array_Variable`

`Variable.nn` - create if(`Variable != null) statement

`Variable.try` - create Try block for this `Varaible`

`Boolean_Variable.while` - create while loop block

`Variable.field` - create a Class member variable with `Variable`'s name

`Variable.return` - create return `Variable` statemen