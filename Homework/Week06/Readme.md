# Homework 6

## Problem 1

Write a program to read  utf8.txt into a Java string ( the file is UTF-8 encoded ) and then save the String into a utf-16 encoded file called 'utf16.txt'

Issue the following commands in your terminal:
```
$ls
# lists files, but no utf16.txt
$ xxd utf8.txt
# output
$ java YourProgram
$ ls
# lists files, now there is a utf16.txt
$ xxd utf16.txt
```

and take a screenshot. This is so I can see that your code created a utf16 file and that the bytes are correct.

## Problem 2

An interesting thing I noticed when reading the Java documentation for `char`/`Character` is that an int type can store a UTF-16 character. As you remember, this is because a UTF-16 code point can be either 16 bits or 32 bits. A Java `char` has only 16 bits, however, so it isn't large enough to store a full 32 bit UTF-16 character.

Write a program that uses the following methods from the Java 8 documentation.

* int 	lastIndexOf(int ch)
* int 	lastIndexOf(int ch, int fromIndex)
* String(int[] codePoints, into offset, int count)

See more about these methods here:
https://docs.oracle.com/javase/8/docs/api/java/lang/String.html

## Problem 3

A feature of Unicode is that there is some redundancy in character representation. I already showed you in UTF-16 that the digits are values \u0030 - \u0039. For confirmation you can look at your ascii table.

There are other Unicode codepoints that represent the digits 0-9. 

Make The Character.isDigit() method return true for one of them.

See the documentation here:
https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html

Hint:

Read this:
https://docs.oracle.com/javase/7/docs/api/java/lang/Character.html#isDigit(int)

## Submission
Due on Blackboard Tuesday Oct. 15th by Midnight. 
Submit:
* Problem1: Code and screenshot
* Problem2: Code
* Problem3: Code

## If you are curious
Good video on youtube about UTF-8
https://www.youtube.com/watch?v=MijmeoH9LT4
The guy has alot of other interesting videos too.
