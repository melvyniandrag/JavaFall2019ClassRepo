# Week 05 Lecture

## Introduction
A lot of material in this lecture. After the lecture, be sure to check out these references:
References:
1. https://www.baeldung.com/java-bitwise-operators
2. https://docs.oracle.com/javase/tutorial/java/nutsandbolts/opsummary.html
3. https://www.youtube.com/watch?v=lKTsv6iVxV4
4. https://www.youtube.com/watch?v=4qH4unVtJkE
5. https://www.geeksforgeeks.org/java-lang-integer-tobinarystring-method/

In this lecture we'll look at the 1s and 0s of various  Java datatypes, and see how to manipulate them.

After this lecture you need to understand:

* Signed shift operators >> and <<
* Unsigned shift operator >>>
* bitwise operations ( ^, &, | )
* Endianness
* Two's complement
* Hex representation of binary numbers
* How to extract certain bits from a byte.


## Motivation
In many scenarios, data is encoded in bits instead of in primitives like int, float, etc.. I do this often at my job.
[show tool and smart phone]
This tool needs to send information to the smartphone about itself. This is so the user can see some data on the smartphone
and operate the tool properly based on this data. For example, the tool will tell the phone if the tool is going:

* forward
* reverse
* not turning

The tool could say this using strings, but then how many bytes does the tool have to send? How many bytes does it take to send the string "forward"?
Now this is a tricky thing - text encodings are a bit tricky. It's going to take at least 7 bytes to send this string f-o-r-w-a-r-d. 

( If you know about compressed binary data, don't answer this, leave it for the folks who don't ) Can we do better? [Any ideas?] 

We could encode 'forward' as '0' and send a single byte - 00000000

We could encode 'reverse' as '1' and send a single byte - 00000001

And then we encode 'not turning' as '2' and send the byte 00000010 to mean 'not turning'.

So now, when the Java code on the smart phone asks the tool if it's turning clockwise, counterclockwise, or not turning at all, the tool only has to send a single byte ( 8 bits ).

Not bad, but . . . 

( No answers from people who know about compressed binary data ) Can we do better? [ Any ideas?]

The answer is yes. We need only 2 bits to send these messages!

forward = 00b = 0
reverse = 01b = 1
notmoving = 10b = 2
and we still have a spare bit pattern "11b" to send. Maybe we could encode "tool is on fire" in that setting or something. So we wet from 7 \* 8 = 56 bits to send "forward" to just 2. Not bad.  

Now we have to see how to play with individual bits in java. So far I've showed you primitive types, which are 1- 8 *bytes*. And then we've looked at complicated objects. Now we will look at bits.

## Binary numbers
A quick discussion of binary numbers
I do this on the board:
* 1 in binary
* 2 in binary
* 3 in binary
* 79  in binary

## Binary exercise 
You compute this in binary:
* 4 in binary
* 110 in binary

## Hexadecimal representation
A byte is made up of 8 bits. It is convenient for many reasons to write a byte in 2 hex characters instread of 8 1s and 0s. How to do this?

How to read hex:

Take a sample binary representation of a byte.

10101011

Split this into two groups of four

1010    1011

Now compute the decimal value of each part


1010 = 10

1011 = 11

Hex numbers go like this: 0, 1, 2, 3, 4, ... 9, ( just like decimal numbers ) but there needs to be six more.

so for counting in hex we do this:

0, 1, 2, 3, 4, 5, 6 ,7, 8, 9, A, B, C, D, E, F

most languages don't care if you write a-f in capital or lowercase. So the above number

10101011

can be written in hex as 

0xAB.

So throughout this class you might see me write `byte b = (byte) 0x11` or the like. Now you know what this means.

Hex is just a compact and easy to understand way to write a binary value. 

## Show an ACK from work

Here is a sample bluetooth message that I would send from my tool to a smartphone:

01101100 10100000 11110000 01011111

Translate this to hex.

Have class do
Hex worksheet

## Preface to the rest of the lecture

So far we haven't done any Java. Now let's apply this information to Java. In Java you often want to manipulate binary values ( 1s and 0s ) instead of primitives or objects. Now we will see how to do this.

7:40 - 8:00 ## Signed bitshifts ( left and right, >> and \<\< ) on positive numbers in Java
Show how to shift bits See `Code/SignedBitshift01.java`

Show how we can shift bits to the right. Start with 127 and shift it right 1, 2, 3, 4, 5, 6 places.

Show how we can shift bits to the left. Start with 1 and shift 1, 2, 3, 4, 5, 6 places

Call attention to the fact that with the left shift we ought to be able to shift the number one more spot to the left. 
Should I show the disaster that happens first, then talk about it, or should I talk about it first, then show you?
[Wait for response]

In order of request:
a) show what happens when you shift the byte 0x01 to the left 7 places
b) mention how Java has no unsigned numbers. So that  bit there is reserved explicitly for the sign of the number. 
In fact, Java uses the "two's complement" representation for signed numbers.

There are different tools for different jobs. For my work, I generally want a language that allows me to shift a bit into that location and not have to worry about the side effects in terms of signs. This has caused some minor irritation on the job as it requires a bit more thought for managing binary data that you don't have to worry about when using C or C++. So why did the number turn negative? I've said it has something to do with the way numbers are stored in Java. Stay tuned and I'll explain what has happened in a few minutes.

8:00 - 8:10 ## Signed right bitshifts ( >> ) of negative numbers in Java
Show how to shift -1 to the right 1, 2, 3, 4, 5, 6 times.

What happens on the seventh?

8:10 - 8:30 ## Unsigned right shifts ( >>> )
You can right shift and, instead of replicating the left most bit, you insert zeros. Note - for all left shifts ( << ) java inserts zeros. For right shifts you have to choose either >> or >>> as you think about what result you want.

See `Code/UnsignedShift.java`

8:30 - 8:50 ## bitwise ops ( and, or, xor )
Java provides three more useful bitshift operations. Namely, and (&), or(|) and xor(^).

See `Code/BitwiseOperations.java`

Show an and, or and xor operation on the board.

This might be confusing. Read the links provided at the top of this document if you want to refresh your memory about what the various things mean. 

As I've hinted - there is some minor trouble in working with negative numbers in Java. I haven't told you how it does it yet, but Java handles negative numbers in a particular way that means you have to think a little bit when you manipulate them.


This is useful for extracting particular bits. If you want all bits, you and your byte with 0xFF. If you want only the last 4, you can `&` your byte with `0x0F` and then shift it wherever you need to shift it to, if you need to.

8:50 - 9:20## Homework Discussion
 
You need to be able to extract bits from numbers 

In class activity  - we will turn a long into a byte in a funky way.
A long has how many bytes in java?? 8
We will convert the double into a byte array.
We will extract the last bit from every byte.
We will shift every bit to the proper spot. 
We will and the shifted bytes so that we have

Construct this long:
0x01, 0x01, ... 0x01


9:20 - 9:40 ## Endianness
If time. Java is big endian. Other languages / platforms are little endian.

This bit of information is important to know for interviews and stuff. If you are working in embedded software, or you do alot of sending data over networks ( internet, bluetooth, lora, etc. ) you need to know this. A picture is worth a thousand words.

How do you write the 4 byte integer "1" in hex?

0x00 0x00 0x00 0x01

Or in binary if you prefer

00000000 00000000 00000000 00000001

( maybe now you see why it is preferable to write in hex. We could just write "1" in decimal, but that doesn't make it clear how many bytes are involved. That's ( one reason ) why we use binary / hex )

On a little endian system, the LOWEST BYTE GOES FIRST!!! Very weird stuff. This is how the machines were implemented. So we would write

0x01 0x00 0x00 0x00 

on a big endian platform we write the largest byte ( most significant byte ) first

0x00 0x00 0x00 0x00

Depending on how you think about it, both of these representations have good reasons for existence.

The Java VM is like a fake computer running on your computer. The java virtual machine is big endian. My real machine, however - the actual circuitry in my computer that the electrons flow through - is an intel machine. Intel architectures are little endian. 

See `Code/IntelIsBlah Blah endian.cpp`

and

`Code/JavaEndianness`

## Endianness exercise


9:40 - 9:50 ## Negative numbers in Java
Java uses two's complement.

To get the negative value of a number in Java, you use the two's complement system.

How java stores a negative number:
1. Take the positive number
2. Flip all the bits
3. Add 0x01 to those bits.

The above steps describe the "two's complement" system.

e.g. short 1 -> short -1

1. 00000000 00000001 ( take the number 1 )
2. 11111111 11111110 ( Flip all the bits )
3. 11111111 11111111 ( Add 1 to it )

