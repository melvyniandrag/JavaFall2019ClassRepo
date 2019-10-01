# Week 05 Lecture

A lot of material in this lecture. After the lecture, be sure to check out these references:
References:
1. https://www.baeldung.com/java-bitwise-operators
2. https://docs.oracle.com/javase/tutorial/java/nutsandbolts/opsummary.html
3. https://www.youtube.com/watch?v=lKTsv6iVxV4
4. https://www.youtube.com/watch?v=4qH4unVtJkE



In this lecture we'll look at the 1s and 0s of various  Java datatypes, and see how to manipulate them.

After this lecture you need to understand:

* Signed shift operators >> and <<
* Unsigned shift operator >>>
* bitwise and operator &
* bitwise or inclusive or |
* bitwise exclusive or
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

## Signed bitshift
Shifts bits, while preserving the 'sign bit' See `Code/SignedBitshift.java`

## Unsigned bitshift


## bitwise ops ( and, or, xor )


## Schedule

* 7:00 - 7:30 Bits, bytes, and binary
* Tow's complement
* 7:30 - 8:00 Hexadecimal notation 
* 8:00 - 8:10 Break
* 8:10 - 8:30 The byte datatype. 
* 8:30 - 9:00 Bitwise operations
* 9:00 - 9:05 Break 

* 9:35 - 9:45 Discuss Homework
