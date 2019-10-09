# File IO, Text Encodings and Exceptions

## [ 7:00 - 7:04 ] Introduction 
Take aways for todays Lecture:
* Know ASCII
* Know UTF8
* Know UTF16
* Know about file IO in Java
* Know about exceptions in Java

I think the stage is now set for us to talk about text encodings and see how they relate to Java.

We've looked at binary encodings a bit, I've had you play around with bits for  a while, and for one assignment I asked you to write to a file. Now we can look at encodings in a meaningful way, thanks to the knowledge we gained from the past exercises.

In the last assignment/lecture we looked at how integers were laid out in binary in Java. Do you remember?

Good. Since we've got that covered: in today's class we aren't talking about integers, we are talking about text. You'll note that Programming languages have numeric types like `int`, `byte`, `long`, `short`. They also have text types like `String` and `char`. I'm sort of done talking about how whole numbers are done in Java. Today we are talking about text. If there is time, we'll talk another day about how floating point numbers ( fractions, decimals ) are stored on computers. That is quite different from how strings and ints are stored. 

But the subject of today is text.

## [7:04 - 7:25 ] ASCII

Back in the beginning, many years ago, there was one standard for characters on a computer. The standard was called ASCII. This set of characters allowed us to express numbers, English language characters, punctuation, and a few other things. In any event, as I said, way back when, before the iPhones and the internet, before Linux and Windows existed, there was a text encoding called ASCII. And ASCII was good and used widely. If you've heard the term ASCII before, I'll bet that you heard it in the context of ASCII art, which is still a funny thing people do.

Things like this:
( taken from https://www.asciiart.eu/animals/marsupials)

Ok, ok, get on with it .What is ASCII??!?!?! ASCII describes one way for laying out 1s and 0s to represent text in a computer. ASCII allows you to represent 128 different values, some are printable and some are not. I'm giving you a chart now - I have one of these on my desk at home, one on my desk at work, and I invariably refer to it at least a few times a week. I'm giving you all a laminated copy in case you have a little desk space you want to fill with something useful.

[Pass out the ASCII table] 

Lets look at the binary representation of some letters in ASCII. Keep in mind - right now we're talking about something applicable to all computers and all programming languages, not just Java.

Convert a, A, b, B, z, Z to binary from the hex in the provided table. Convert 0 to binary from the table. 

[ Exercise ] 
Note the number of values in the ASCII table. How many are there? I just told you - 128. How many bits is that? [ Have everyone write the answer on a piece of paper while Jeopardy music plays. ]

Answer: 7 bits

[/Exercise ]

Enough said. So by now you understand ASCII. I'll just give you a few questions to cement your knowledge of ASCII, okay?

## Exercise 1 - 1 minute
## Exercise 2 - 2 minutes
How many bytes are in a java Character?

Hint:
Have a look at the Character class. I think there is a Character.NUM_BYTES field, I can't remember. Use google and your references to find the size of a Java character.

## [7:25 - 8:15 ] UTF-16 and Java
Weird! So Java allocates two bytes for a single Character, but we've seen that in ASCII you only need 7 bits!!! What is going on here?

See, when ASCII came out, this was many years ago. 40, 50 years ago ( I'm not sure when the standard was adopted, there is alot of history here ) computers and the related technology were being developed in the USA and in the UK. You see this heritage today because all of the programming languages, all of the Linux command line tools, all of the system calls in various operating systems - they are all in English! That's because all of the original system design and everything was done in English speaking countries, and now English has also taken over math and science, beating out competing languages like French, Russian and German. So for a long time there was no reason to really think about an alternative to ASCII. 

But in the 80s and 90s and the PC was developed, international customers started buying and using computers - computers stopped being huge machines that only University and Government labs could afford. And so it became evident that ASCII wasn't enough. Customers in Spain needed a way to represent '~n' in text files. Germans needed a way to type 'B/ss' on their computers. In Russia, the people needed a way to type their cyrillic letters like 'f/cyrillic f' etc. And so a bunch of alternative encodings sprung into existence. Remember that ASCII used only 7 bits! So that left an additionaly 128 characters that could be represented by turning on the first bit in the octet. ve 

How many letters are there in Cyrrillic?

How many chinese characters are there?

How many Japanese characters are there?

You'll see, as others saw, that this was a mess. Every other language needed some special characters. Some languages needed more than the 128 provided by turning on the extra bit.

It was about this time that Java was being developed. Java v. 1 was released in the early 1990s - correct me if I'm wrong, around the time that a new standard for text encoding was released - Unicode! Unicode was a description of how to represent characters from many languages, through the use of a new encoding. 

There are/were several incantations of Unicode. When Java was first designed, thanks to several factors, the front runner was an encoding called UTF-16, which required - can you guess how many bits? [Wait] nad how many bytes? [wait] hence the size of a Java character being 2.

Have a look at Java. Java strings store utf16 characters. Now try and wrap your head around this - compile, run and inspect the output of the `StringLength.java` code. 
You will see that Java says that string has length 5. Very weird.
How about the lenght of the string "hello"? Why does that say "5"? So does the fyodor sequence. So what does the Java String length() function count? The number of bytes or the number of characters? So far as I can tell, it counts characters.

It is hard to type anything but ascii on a keyboard. Here's how to create non-ascii characters in Java

* copy and paste the character from another source e.g. the internet. ( Already shown with the 'Fyodor' example )
* \uXXXX syntax ( only works with 2 byte characters ) See `Code/UXXXX.java`
* Write an int array and convert to string. ( works with 2 or 4 byte characters ) See `Code/IntArrToString.java`.


### Writing some characters with `\uXXXX` syntax

Show how to write a Java string for something french using the uxxxx syntax. e.g. Have Czerinton write "hello" in Tamil on the board. Then get the unicode points from the table here:
https://www.fileformat.info/info/unicode/block/tamil/list.htm

Then try to write it out in a java string using the "\uXXXX" syntax.


### Writing some characters with the "cut paste method" - risky.
Show an example

### Handling 32 bit utf-16 characters in Java - bad documentation or a late language addition?

Unfortunately for the Java language designers, shortly after the language design was finalized the Unicode people decided that 2 bytes wasn't enough to support 

Print some emojis with Java.

I was looking at this example:

```
String s = "😂";
int emoji = Character.codePointAt(s, 0);
String unumber = "U+" + Integer.toHexString(emoji).toUpperCase();
System.out.println(s + "  is code point " + unumber);
String s2 = new String(new int[] { emoji }, 0, 1);
System.out.println("Code point " + unumber + " converted back to string: " + s2);
System.out.println("Successful round-trip? " + s.equals(s2));
```

from here: https://stackoverflow.com/questions/43236842/how-should-i-represent-a-single-unicode-character-in-java

And noticed there was a constructor for `String` that went `String(int[] intArr, int a, int b)`. I went to the docs to find it and see what it did, so I googled 'Java String constructors'. The result that came up was the Java 7 documentation:

https://docs.oracle.com/javase/7/docs/api/java/lang/String.html

I just changed the 7 to 8 in the url and ( thank goodness ) it worked and I found the `String( int[] . . .)`  constructor. 

https://docs.oracle.com/javase/8/docs/api/java/lang/String.html

Wonder if this was a language addition in 8, or just patchy documentation for 7.

### Surrogate pairs
Show an example of a surrogate pair.

```
//http://www.informit.com/articles/article.aspx?p=2274038&seqNum=10
See `Code/Surrogate.java`
```

### Wikipedia
Look at the table here in class

https://en.wikipedia.org/wiki/UTF-16


## [ 8:15 - 8:35 ] UTF-8

UTF-8 is the best thing you can learn. I love it. It's easy to learn, and super fun to learn.
It's so easy to understand, but I fear you may be scared of the things I'm saying in class. There is a famous youtuber called TomScott. For homework you have to watch his video.
 Explanation of how it works. 

 Run `Code/StringToUTF8.java`

## [ 8:35 - 9:00 ] File I/O

### Reader/Writer Example
FileReaderExample stinks. It has a number of flaws which you may understand now. From my tests, it seems to work fine for ASCII text, but is not good for other text. I can think of some ways to trick it into working, but I'm lazy and don't want to do more work than I have to . There must be something better ( we'll see soon) than FileReaderExample.
Look at `Code/FileReaderExample.java`. Also note that I have utf8 and utf16 versions of the input files. To convert I used a cool command line tool available on linux, (maybe mac) and ( maybe windows ).

```
$ iconv -f UTF-8 -t UTF-16BE dostoyevsky-utf8.txt > dostoyevsky-utf16.txt
```

Use `xxd` or other hex analyzer to verify that the file converted correctly. Check , for example, the UTF-8 and UTF-16 hex encodings for the cyrillic 'f' - 'Ф'. Look here: https://www.fileformat.info/info/unicode/char/0424/index.htm.

An interesting thing about this iconv - note that I specified UTF-16BE. Bonus points for "superb mental performance" for whoever can tell me what the "BE" stands for. I'll hint that there is also an "LE" option. 

If you're not sure you can run the iconv method with LE and look at the output, compare to BE.

Wait a minute for the answer.

## What is your system's default encoding?

We will first look at our System's default encoding. Let's all run this example and see what we get. Probably we will all have different charsets.

```
import java.nio.Charset;
public class Encoding{
	public static void main(String[] args){
		Charset defaultCharset = Charset.defaultCharset();
		System.out.println(defaultCharset.toString());
	}
}
```

Have everyone run the example. If we all don't have different charsets, then that is a crazy coincidence. In general, different platforms have different encodings and for this reason I am inclined to not use the FileReader / FileWriter classes. These classes use the machine's default encoding, but we have no way to know what this on every machine our code may run on. This could cause serious problems for our software.

### Buffered reader

### To Bytes


## [ 9:00 - 9:10 ] War story.

I'm speaking from experience on this. When I worked for a machine learning company for a few years, we did machine learning on terabytes of social media data. We would gather text from facebook, twitter, instagram, blogs, etc, and we would categorize people based on their posts. Then we would sell this data to advertising agencies as well as other companies so they could target people with ads based on personality type. Kind of scary stuff, but the pay was good.

Before even doing any cool machine learning, we had to go through great efforts to ensure that we knew the text encodings for every piece of text we had. This was a bit of a challenging technical problem, but not too bad. If we would have run an algorithm on a mixture of utf8, utf16, and utf32 encoded text the results owuld have been garbage! 

I won't tell you the boring details of how we checked text encodings - now you know enough to write your own code to check text encodings. This information is very important for machine learning, for writing international websites, for writing apps - when you write an android app and a user copies from text from a page in your app to paste it into google - what encoding is that text? Will google recognize it? Will your browser crash?

This is important to know.

## [ 9:10 - 9:12 ] I hate Quora
Half of the questions there are moronic or subjective. 

And then most answers to the questions are simple, wrong, written by n00bs. Like this one:
https://www.quora.com/How-do-I-program-an-emoji-in-Java

Some tool named "Prince John" gave an incorrect, one sentence answer to a huge question that we still haven't quite answered in 3 hours of lecture.

and on top of that, they ask you to register with the site to use it.

I invite you to join me in not clicking quora links on google. I accidentally clicked this one and, as usual, was dissapointed by the output.


## [ 9:12 - 9:13 ] The proverb
At the beginning of this class I told you that the Java mantra is "write once, run everywhere." Hopefully by now you realize that the better manta, that many have long accepted, is :

Java is "write once, debug everywhere"

## Exceptions

Exceptions are quite controversial in computer programming. The way they are implemented, whether or not they should exist, etc. are all contested. We aren't implementing programming languages or studying language theory in this class, so the pros and cons of exceptions aren't relevant to us in this class - I'll just drop some notes here in case you already know about exceptions and want to read more:
* https://softwareengineering.stackexchange.com/questions/107723/arguments-for-or-against-using-try-catch-as-logical-operators
* https://petercai.com/the-case-against-exceptions/
* https://stackoverflow.com/questions/613954/the-case-against-checked-exceptions
* https://www.atlassian.com/blog/archives/exceptions_are_bad

If you want to learn about languages that don't have exceptions, read here:
https://stackoverflow.com/questions/35343584/which-programming-languages-dont-have-runtime-exceptions 

Whether you like them or hate them, exceptions exist in Java and are a fundamental part of the language. The java code you read and write will use exceptions, so you need to learn alot about them. I'll show you an example and then we'll talk more about them.

See `Code/Exceptions`

Things to look out for here are:

* throws
* throw
* try
* catch
* Exception
* IllegalArgumentException ( and other specific exceptions )

## Exercise
Type up the code samples here for BUILT IN EXCEPTIONS:

https://www.geeksforgeeks.org/types-of-exception-in-java-with-examples/

Skip over the User defined exceptions. 

Just take note that you can program your own special error scenarios if you want to.

For now it is sufficient to get a handle on using the standard Java exceptions.

## Exercise triggering specific exceptions

Write a function that throws FileNotFoundException. Something like:

```
public void g() throws FileNotFoundException{
	throw FileNotFoundException("foobar");
}
```

call `g()` in `main()` and handle the exception.

Remove the throws keyword from g and see if it compiles. Explain.

## Exercise 2 with Exceptions

Try to trigger an interrupt exception with Java. Look back at the try/catch exceptions in Java and try to trigger an interrupt exception.

See `Code/InterruptException`

To be honest I haven't given much thought to all the different scenarios that InterruptExceptions might arise in. I just put in enough effort to be able to trigger one and get a passing understanding. Sometimes, your computer might interrupt one of your threads for one reason or other. Just be aware that this can happen and that there are mechanisms for dealing with it.
## [ 9:40 - 9:45 ] Homework

Discuss the homework

## [ 9:44 - 9:45 ] Hacktoberfest

If you want a tshirt you should talk to someone about Hacktoberfest. You can get experience in open source and get a tshirt. 

I had hoped to hold a hackathon here last weekend, but there didn't seem to be much enthusiasm from my students so I went fishing instead.
If you are interested, talk to your classmates, see who knows about git and github. Or you can ping me and I'll give you more information.

## References:
Complete UTF16 listing = http://www.fileformat.info/info/charset/UTF-16/list.htm
