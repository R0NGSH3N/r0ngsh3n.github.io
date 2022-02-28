---
layout: post
title: Implement Guava by Examples
tags: Java Guava
categories: common
---

Guava is a set of core Java libraries from Google that includes new collection types (such as multimap and multiset), immutable collections, a graph library, and utilities for concurrency, I/O, hashing, caching, primitives, strings, and more! It is widely used on most Java projects within Google, and widely used by many other companies as well.

## String Manipulations

### `Joiner` and `Spliter`

- Convert String List to String and join all the elements by ",":

~~~Java
    String join = Joiner.on(",").skipNulls().join(Lists.newArrayList("a", "b", null));
    String join2 = Joiner.on(",").useForNull("null").join(Lists.newArrayList("a", "b", null));
~~~

in here, we use `skipNulls()` of `Joiner`, so if there is null value in the list, this will NOT throw NullPointerException, but you also can use `useForNull(String)` to replace null value. the second line of code will print "null" instead of skip.

- Split String by "," and convert into String List

~~~Java
    for(String element : Splitter.on(",").trimResults().omitEmptyStrings().split(" a, , b,, c, ")){
        System.out.println(element);
    }
~~~

in here, we split a CSV format of String and convert to String ArrayList, we use `omitEmptyString()` to skip the empty element and use `trimResult()` method to remove the blank space before and after the String.

### Use `CharMatcher`

- use `CharMatcher` to determine Character in a String:

~~~Java
    System.out.println(CharMatcher.is('o').matchesAllOf( "ooo")); //true
    System.out.println(CharMatcher.is('o').matchesAnyOf( "ooo")); //true
    System.out.println(CharMatcher.is('p').matchesAnyOf( "ooo")); //false
    System.out.println(CharMatcher.is('p').matchesNoneOf( "ooo")); //true
~~~

- use `CharMatcher` to determine the position of Character in String or number of occurrence

~~~Java
    System.out.println(CharMatcher.is('b').indexIn( "abc")); //1
    System.out.println(CharMatcher.is('b').lastIndexIn( "abcb")); //3
    System.out.println(CharMatcher.is('b').countIn( "abcb")); //2
~~~

- use `CharMatcher` to manipulate the String

replace any Character or Character combination with another Charactor

~~~Java
    System.out.println(CharMatcher.anyOf("b").collapseFrom("abcb", '-' )); // return "a-c-"
    System.out.println(CharMatcher.anyOf("bc").collapseFrom("abcbc", '-' )); // return "a--"
~~~

