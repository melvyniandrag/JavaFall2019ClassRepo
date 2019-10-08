# Homework

## Problem 1

Read the attached utf-8 file ( `utf8.txt` ) into a Java String.

Write the string to utf-16 encoded file ( but save it as `utf8.txt` )

Use `xxd` or a similar hex-dump tool to allow me to see the hex characters inside the file.

Run your program again. This time it will be reading a utf16 file. It should generate some error because the file encoding is wrong.


## Problem 2

An interesting thing I noticed when reading the Java documentation for `char`/`Character` is that an int type can store a UTF-16 character. As you remember, this is because a UTF-16 code point can be either 16 bits or 32 bits. A Java `char` has only 16 bits, however. 


