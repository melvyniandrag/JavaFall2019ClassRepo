# Week 3 Lecture
We've so far covered some basic java information and I expect you are able to write 
and compile simple Java programs. Amongst the things you now about are:

* classes
* inheritance
* interfaces
* the final keyword
* the static keyword
* various primitive data types e.g. int, double, byte, char[], etc.
* javac \& java
* difference between compiled and scripting languages
* Java code is cross platform
* for loop
* while loop
* if
* switch
* etc.

while this information is enough to do interesting work, there is still much more
to know about the language. Tonight we're going to add the following things to your
bag of java tricks:

* [ 7:00 - 7:15 ] Numbers
* [ 7:15 - 7:30 ] Strings
* [ 7:30 - 7:45 ] Exercises
* [ 7:45 - 8:00 ] Packages in Java
* [ 8:00 - 8:15 ] Compile and Run Astronomy Examples
* [ 8:20 - 8:40 ] Exceptions
* [ 8:40 - 8:50 ] Exercise triggering specific exceptions
* [ 8:50 - 9:10 ] Threads + concurrency
* [ 9:10 - 9:20 ] Exercise with multithreading
* [ 9:20 - 9:35 ] File I/O
* [ 9:35 - 9:42 ] Exercise with File I/O
* [ 9:42 - 9:45 ] Homework discussion.

## [ 7:00 - 7:15 ] Numbers

*Reference: https://docs.oracle.com/javase/tutorial/java/data/index.html*

In Java we generally work with objects in our code, instead of primitives. I presented primitives over the last few weeks like int, float, double, byte, etc. Today I'm going to show you that Java hasa analogues of these primitives that are objects. These classes are called 'Byte', 'Double', 'Float', 'Integer', 'Short' and 'Long', and all are implementations of the Number class. This means that somewhere in the source code of the Java language there is something like:

```
public class Integer extends Number{
 // does stuff
}
```

The reasons that these things exist can be read about in the documentation I've linked above. I'm just going to show you how to use these Number classes.

### All subclasses have these methods
According to the documentation. I haven't verified all of these methods, but they should be there according to the language specification.

[ see documentation ]

### Some methods are just for individual classes. 
The following methods are only for the Integer class. 

[ see documentation ]

See the code in `Code/Numbers` for some examples

### Printing numbers

So far we've only seen `System.out.println()` and `System.out.print()`. There are other printing options in Java, such as `System.out.format()`. `format()` has a bunch of cool options to alter how you display things like numbers, and even has a platform-independent newline character! ( Remember, on Linux and BSD (macOS and others ) a newline is just '\n', but on windows the newline is '\r\n'

```
// the %d says 'print me as an integer'
// the %n is a platform-independet version of '\n'
// ( On windows I'd expect format("%n") to print out '\r\n' )
int i = 461012;
System.out.format("The value of i is: %d%n", i);
```

See `Code/PrintingNumbers`

## [ 7:15 - 7:30 ] Strings

*Reference: https://docs.oracle.com/javase/tutorial/java/data/index.html*

A String in Java is a sequence of characters. Unlike some other languages, a String in Java is an object, not a primitive. The reasons for this are quite interesting and, as I always say, we won't be able to discuss that until later.

You've seen so far how to get Strings out of numeric types with things like `ToString()` and you also saw how to get a number from a String with `String.valueOf()`

The main things you'll probably want to do with Strings for now is concatenate them. That means to add them together. You can do that using `+` or the `.concat()` method.

```
String s1 = "Hello";
String s2 = " ";
String s3 = "World";
String s4 = s1.concat(s2).concat(s3);
```

You can also make formatting strings. We already saw these in the `.format()` method. But you can take the string out of the method and put it as it's own variable.

```
String fs;
fs = String.format("The value of the float " +
                   "variable is %f, while " +
                   "the value of the " + 
                   "integer variable is %d, " +
                   " and the string is %s",
                   floatVar, intVar, stringVar);
System.out.println(fs);
```

For more information about String formatting look at the table here:

dzone.com/articles/java-string-format-examples


See `Code/Strings`.

## [ 7:30 - 7:45 ] Exercises

1. Write a program using `Byte` objects that uses methods similar to the methods above that were only for the Integer class. e.g. does 	`static Byte decode ( String b )` exist? Does `static Byte valueOf(String s )` exist?

2. In the documentation there is also a reference about how to print dates.

```
import java.util.Calendar;
import java.util.Locale;

public class TestFormat {
    
    public static void main(String[] args) {
      long n = 461012;
      System.out.format("%d%n", n);      //  -->  "461012"
      System.out.format("%08d%n", n);    //  -->  "00461012"
      System.out.format("%+8d%n", n);    //  -->  " +461012"
      System.out.format("%,8d%n", n);    // -->  " 461,012"
      System.out.format("%+,8d%n%n", n); //  -->  "+461,012"
      
      double pi = Math.PI;

      System.out.format("%f%n", pi);       // -->  "3.141593"
      System.out.format("%.3f%n", pi);     // -->  "3.142"
      System.out.format("%10.3f%n", pi);   // -->  "     3.142"
      System.out.format("%-10.3f%n", pi);  // -->  "3.142"
      System.out.format(Locale.FRANCE,
                        "%-10.4f%n%n", pi); // -->  "3,1416"

      Calendar c = Calendar.getInstance();
      System.out.format("%tB %te, %tY%n", c, c, c); // -->  "May 29, 2006"

      System.out.format("%tl:%tM %tp%n", c, c, c);  // -->  "2:34 am"

      System.out.format("%tD%n", c);    // -->  "05/29/06"
    }
}
```

compile this code. Change the dates and times. 

There is an interesting number called 'euler's number' in math class. Replace the math examples with Euler's number and see how it prints.

3. Try String.valueOf() with different types as mentioned here:

javapoint.com/java-string-valueof

4. Print some formatting strings using each and every one of the options in the table at the dzone link.

5. Try parseInt() and parseByte(). Try parseByte() with value 300. See `Code/Parse`. You can't parse 300. What's the biggest you can parse?

##  [ 7:45 - 8:00 ] Packages in Java

*Reference: https://docs.oracle.com/javase/tutorial/java/package/index.html*

Purpose of Packages:
See "Creating and Using Packages" at the reference above.

* It communicates to other developers that the files in a package are related
* It helps you remember that a set of .java files are related - trust me, you'll forget what you were doing if you walk away from some code for 6 months or a year.
* Gives you some isolation for different names. E.g. you can implement the method f() in several packages so they don't conflict! ( We need to see an example of this, this is quite imporant. Packages provide a 'namespace' for functions. This is important, but you probably won't realize how important packages/namespaces are until you hit a related bug while you are on the job, writing code ,and it blows up in your face. Nevertheless, I'll try to show you.
* You can allow classes in a package to communicate, but restrict the access outside the package .This goes back to our discussion or public vs private. Sometimes you want classes in a package to be able to exchange certain bits of data, but you don't want classes outside the package to use this data.

Let's walk through the samples in `Code/Packages` now together.

1. Run through the Example01.
2. Then change the access specifiers and see what happens
3. Run through Example 02, showing how I added another level 

## [ 8:00 - 8:15 ] Compile and Run Astronomy Examples. 

Have class download the class repo ( use git if they have it, download the zip if they dont. )

Walk through downloading and compiling the code quickly so that hopefully a few people understand.

Show the class a few samples running.

Then have the people who understood go and explain to the rest of the class.

Wait until everyone can run the few sample programs I listed.

Have the class look through the code and see that the java files have pacakge rsf.x.y.z at the top. The astro code isn't mine, I foudn it in a cool book. This is to show that packaging really works.

## [ 8:15 - 8:20 ] One more thing about packages

Compile two packages with a method of the same name. Compile these packages, create a new Main class and use both classes in your Main.

See `Code/Packages/Example03`

## [ 8:20 - 8:40 ] Exceptions




## [ 8:40 - 8:50 ] Exercise triggering specific exceptions

## [ 8:50 - 9:10 ] Threads + concurrency

*Reference: https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html*

## [ 9:10 - 9:20 ] Exercise with multithreading

## [ 9:20 - 9:35 ] File I/O

*Reference https://www.geeksforgeeks.org/file-class-in-java/*

## [ 9:35 - 9:42 ] Exercise with File I/O

## [ 9:42 - 9:45 ] Homework discussion.

 
