# Week 02 Lecture Notes

Last week we learned alot about Java already. You've already written some programs that exercise alot of Java functionality. Going forward I'm going to present more and more concepts. Java is a relatively simple language and you already know quite a bit. Don't worry, within the next lecture or two you'll know everything you need to know to write interesting  code. From then on out I'll just present refinements and additional tools you can use to be an even better programmer.

## Schedule
* 7:00 - 7:15 Review
* 7:15 - 7:30 Inheritance
* 7:30 - 7:35 final keyword
* 7:35 - 8:00 Access Specifiers
* 8:00 - 8:05 Break
* 8:05 - 8:25 Enum classes
* 8:25 - 8:50 The static keyword
* 8:50 - 9:00 Interface and extends
* 9:00 - 9:05 Break
* 9:00 - 9:35 Exercises
* 9:35 - 9:45 Discuss Homework

## [ 7:00 - 7:15 ] Review
We will take a few minutes to review what we've learned so far. 

### Look at the various datatypes we saw last time
Look at the sizes of different datatypes. We will write a class with some methods to check out the sizes of different data types.

The code we write will verify what the Java documentation says about various datatypes here:

https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html

See `Code/DataSizes.java`

## [ 7:15 - 7:30 ] Inheritance
https://docs.oracle.com/javase/tutorial/java/IandI/index.html

A key concept in OOP is inheritance. The objects( classes ) we create are often extensions, or more detailed versions, of a simpler object. For example, the `Dog`, `Cat` and `Mouse` classes could all be more refined specifications of the `Animal` class. All animals have certain properties, such as:
* The noise they make
* The food they eat
* etc.

But they all have unique properties as well. We express this relationship in Object Oriented Programming through Inheritance.

See `Code/Animals`.

Compile all java files. Run `java Main`. Then experiment with removing the `extend` keyword from the derived classes. Then try to call a Dog method from the cat instance. Try to call a cat method from the dog instance.

## [ 7:30 - 7:35 ] The final keyword
Show the `final` keyword in action for classes and variables.

Class - we saw inheritance just now. The `final` keyword for a class just means the class can't be extended anymore.

Variable - this means that the value of the variable can only be assigned once. If you know C/C++ you'll know the `const` keyword. `final` is like that, but better in some sense. Truly, the `final` keyword is one of my favorite parts of Java.

See `Code/Final`

## [ 7:35 - 8:00 ] Access Specifiers
I think by this time I've labelled some variables and functions as `public` or `private`, though I probably haven't used `protected` yet. In any event, you may have been wondering what those words mean. Sometimes I may have omitted `public`/`private`/`protected` altogether. We are going to discuss that now.

## [ 8:00 - 8:05 ] Break

## [ 8:05 - 8:25 ] Enum classes

## [ 8:25 - 8:50 ] The static keyword
Search this page for `static` and read all it has to say:
https://docs.oracle.com/javase/tutorial/java/javaOO/classvars.html

## [ 8:50 - 9:00 ] Interface and extends
https://docs.oracle.com/javase/tutorial/java/IandI/index.html

> In the Java programming language, an interface is a reference type, similar to a class, that can contain only constants, method signatures, default methods, static methods, and nested types.

An interface is like a base class, as we saw in our inheritance example, but one that you'd never want to use by itself. Interfaces are very common in Java, you'll come across them quite often in your career. You'll often see interfaces prefixed with I-.

## [ 9:00 - 9:05 ] Break
A short break.

## [ 9:00 - 9:35 ] Exercises

## [ 9:35 - 9:45 ] Discuss Homework

