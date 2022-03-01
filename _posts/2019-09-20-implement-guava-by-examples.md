---
layout: post
title: Implement Guava by Examples
tags: Java Guava
categories: common
---

Guava is a set of core Java libraries from Google that includes new collection types (such as multimap and multiset), immutable collections, a graph library, and utilities for concurrency, I/O, hashing, caching, primitives, strings, and more! It is widely used on most Java projects within Google, and widely used by many other companies as well.

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

- use `CharMatcher` to determine if String is all digital

~~~Java
    System.out.println(CharMatcher.javaDigit().matchesAllOf("12")); //true 
    System.out.println(CharMatcher.javaDigit().matchesAnyOf("12KD")); //true 
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

trim String

~~~Java
    System.out.println(CharMatcher.anyOf("-").trimFrom("abc---")); //return "abc"
~~~

### Use `Bytes/Shorts/Ints/Longs/Floats/Doubles/Chars/Booleans`

- use `Ints` to initialize the Integet List

~~~Java
    List<Integer> list = Ints.asList(1,2,3,4,5);
~~~

- use `Ints` to join Integers into String

~~~Java
    System.out.println(Ints.join("|", 1,2,3,4)); //return "1|2|3|4"
~~~

- use `Ints` to find max and min in array

~~~Java
    System.out.println("max: " + Ints.max(list.stream().mapToInt(e -> e).toArray()));
    System.out.println("max: " + Ints.min(list.stream().mapToInt(e -> e).toArray()));
~~~

- use `Ints` to check if array contains the number

~~~Java
    System.out.println(Ints.contains(list.stream().mapToInt(e -> e).toArray(), 6)); // return false
~~~

- use `Ints` to convert ArrayList to int array

~~~Java
    int[] arr =  Ints.toArray(list);
~~~

### Use `Multiset` and `Multimap`

in Java:

Order: Yes | Duplicate Yes --> List
Order: No  | Duplicate No  --> Set

Guava add `Multiset` to as

Order: No | Duplicate: Yes --> MultiSet

`Multimap` allow you to have duplicate key to avoid the <key, List<Object>> way in Java:

~~~Java
    Multimap<String, String> multimap = ArrayListMultimap.create();
    multimap.put("sr", "18");
    multimap.put("sr", "male");

    Collection<String> values = multimap.get("sr"); //return [18, male]
~~~

~~~Java
    Multiset<String> multiSet = HashMultiset.create();
    multiSet.add("a");
    multiSet.add("a");
    multiSet.add("b");

    System.out.println("size:" + multiSet.size()); //return 3
    System.out.println("count a:" + multiSet.count("a")); //return 2
~~~

### Use `ImmutableXXX`

`ImmutableXXX` include ImmutableList/ImmutableSet/ImmutableSortedSet/ImmutableMap, here is the example how to implement `ImmutableMap`

~~~Java
    ImmutableMap<String, String> map = ImmutableMap.of("name", "sr", "age", "18");
    for(String key: map.keySet()){
        System.out.println(("key: " + key + " value: " + map.get(key)));
    }
    //key: name value: sr 
    //key: age  value: 18
~~~

### Use `BiMap`

We can use `BiMap` to reverse lookup: value -> key lookup

~~~Java
    BiMap<String, String> biMap = HashBiMap.create();
    biMap.put("sr", "18");

    System.out.println(biMap.get("sr")); //return 18
    System.out.println(biMap.inverse().get("18")); //return sr
~~~
