# Homework 11
In class this week we learned a bit about three java collections - the TreeSet, HashSet and LinkedHashSet. These Collections implement the Collections interface. In this homework I just want you to aquaint yourself with some of the methods offered by these classes as we won't have time to go over all of them in class.

A complete list can be found here:
https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html

For homework I want you to use the following methods:
* add(E e)
* addAll(Collection<? Extends E> c)
* clear()
* contains(Object o)
* containsAll( Collection<?> c)
* isEmpty()
* toArray() *Note, not toArray(T[] a)* 

Some of the method signatures above look scary, but they aren't. This is a good exercise in reading scary looking documentation. Technical documentation always makes the simplest things look awful.

## Problem 1

Write a java program called *MyTreeSetTest.java* and use all of the methods listed above with a TreeSet<Integer>.

## Problem 2

Do the same as Problem 1, but use a HashSet<Integer>. Call the program *MyHashSetTest.java*

## Problem 3

Do the same as Problem1 and Problem2, but use a LinkedHashSet<Integer>. Call the program *MyLinkedHashSet.java*

## Style points
Make sure you create your lists using

```
Set<Integer> l = TreeSet<Integer>...etc....
```

and 

```
Set<Integer> l = HashSet<Integer>...etc....
```

and

```
Set<Integer> l = LinkedHashSet<Integer>...etc....
```

as this is good java style. Java programmers recommend against doing this:

```
HashSet<Integer> l = new HashSet<Integer>...
```

## Submission Guidelines
Submit your three java files on Blackboard before Midnight of Tuesday, November 19th. No two submissions may be the same. I encourage you to work together, but if you do worktogether, change variable names and values in your code so it isn't the same as your friend's.
