# Homework 10
After a long reprieve, we will finally have another homework assignment.

In class this week we learned a bit about two java collections - the LinkedList and the ArrayList. These Collections implement the Collections interface. In this homework I just want you to aquaint yourself with some of the methods offered by these classes as we won't have time to go over all of them in class.

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

Write a java program called *MyArrayListTest.java* and use all of the methods listed above.

## Problem 2

Do the same as Problem 1, but use a LinkedLint<Integer>. Call the program *MyLinkedListTest.java*

## Style points
Make sure you create your lists using

```
List<Integer> l = LinkedList<Integer>...etc....
```

and 

```
List<Integer> l = ArrayList<Integer>...etc....
```

as this is good java style. Java programmers recommend against doing this:

```
LinkedList<Integer> l = new LinkedList<Integer>...
```

## Submission Guidelines
Submit your two java files on Blackboard before Midnight of Tuesday, November 12th. No two submissions may be the same. I encourage you to work together, but if you do worktogether, change variable names and values in your code so it isn't the same as your friend's.
