# File IO, Text Encodings and Exceptions

I think the stage is now set for us to talk about text encodings and see how they relate to Java.

We've looked at binary encodings a bit, I've had you play around with bits for  a while, and for one assignment I asked you to write to a file. Now we can look at encodings in a meaningful way, thanks to the knowledge we gained from the past exercises.

## ASCII

Back in the beginning, many years ago, there was one standard for characters on a computer. The standard was called ASCII. This set of characters allowed us to express numbers, English language characters, punctuation, and a few other things. In the last assignment/lecture we looked at how integers were laid out in binary in Java. Do you remember? Let's take a vote. Are Java integers signer or unsigned?

[Votei, Jeopardy style]
And you remember that the first byte was always the sign bit?

And can you remember if Java integers are Big Endian or Little Endian?

[Vote, Jeopardy style]

Good.In today's class we aren't talking about integers, we are talking about text. You'll note that Programming languages have numeric types like `int`, `byte`, `long`, `short`. They also have text types like `String` and `char`. I'm sort of done talking about how whole numbers are done in Java. Today we are talking about text. If there is time, we'll talk another day about how floating point numbers ( fractions, decimals ) are stored on computers. That is quite different from how strings and ints are stored. 

In any event, as I said, way back when, before the iPhones and the internet, before Linux and Windows existed, there was a text encoding called ASCII. And ASCII was good and used widely. If you've heard the term ASCII before, I'll bet that you heard it in the context of ASCII art, which is still a funny thing people do.

[Show Some ASCII art]

Ok, ok, get on with it .What is ASCII??!?!?! ASCII describes one way for laying out 1s and 0s to represent text in a computer. ASCII allows you to represent 128 different values, some are printable and some are not. I'm giving you a chart now - I have one of these on my desk at home, one on my desk at work, and I invariably refer to it at least a few times a week. I'm giving you all a laminated copy in case you have a little desk space you want to fill with something useful.

[Pass out the ASCII table] 

Lets look at the binary representation of some letters in ASCII. Keep in mind - right now we're talking about something applicable to all computers and all programming languages, not just Java.

Convert a, A, b, B, z, Z to binary from the hex in the provided table. Convert 0 to binary from the table. 

[ Exercise ] 
Note the number of values in the ASCII table. How many are there? I just told you - 128. How many bits is that? [ Have everyone write the answer on a piece of paper while Jeopardy music plays. ]

Answer: 7 bits

[/Exercise ]

Enough said. So by now you understand ASCII. I'll just give you a few questions to cement your knowledge of ASCII, okay?

## Exercise 1
How far apart ( in decimal ) are a and A?
How far apart ( in decimal ) are b and B?
How far apart ( in decimal ) are z and Z?

How is the number 0 stored in binary in Java on your computer?
How is the Character 0 stored in binary in the ASCII encoding?

How is the number 1 stored in binary in Java on your computer?
How is the Character 1 stored in binary in the ASCII encoding?

## Exercise 2
How many bytes are in a java Character?

Hint:
Have a look at the Character class. I think there is a Character.NUM_BYTES field, I can't remember. Use google and your references to find the size of a Java character.

## UTF-16 and Java
Weird! So Java allocates two bytes for a single Character, but we've seen that in ASCII you only need 7 bits!!! What is going on here?

See, when ASCII came out, this was many years ago. 40, 50 years ago ( I'm not sure when the standard was adopted, there is alot of history here ) computers and the related technology were being developed in the USA and in the UK. You see this heritage today because all of the programming languages, all of the Linux command line tools, all of the system calls in various operating systems - they are all in English! That's because all of the original system design and everything was done in English speaking countries, and now English has also taken over math and science, beating out competing languages like French, Russian and German. So for a long time there was no reason to really think about an alternative to ASCII. 

But in the 80s and 90s and the PC was developed, international customers started buying and using computers - computers stopped being huge machines that only University and Government labs could afford. And so it became evident that ASCII wasn't enough. Customers in Spain needed a way to represent '~n' in text files. Germans needed a way to type 'B/ss' on their computers. In Russia, the people needed a way to type their cyrillic letters like 'f/cyrillic f' etc. And so a bunch of alternative encodings sprung into existence. Remember that ASCII used only 7 bits! So that left an additionaly 128 characters that could be represented by turning on the first bit in the octet. 

How many letters are there in Cyrrillic?

How many chinese characters are there?

How many Japanese characters are there?

You'll see, as others saw, that this was a mess. Every other language needed some special characters. Some languages needed more than the 128 provided by turning on the extra bit.

It was about this time that Java was being developed. Java v. 1 was released in the early 1990s - correct me if I'm wrong, around the time that a new standard for text encoding was released - Unicode! Unicode was a description of how to represent characters from many languages, through the use of a new encoding. 

There are/were several incantations of Unicode. When Java was first designed, thanks to several factors, the front runner was an encoding called UTF-16, which required - can you guess how many bits? [Wait] nad how many bytes? [wait] hence the size of a Java character being 2.


### Handling 32 bit utf-16 characters in Java

## Bad documentation or a language addition?
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

## UTF-8

## File I/O
So now we know a few ways that text can be encoded by a computer. When I first came to Java I felt really worried about how to read and write files because there were so many options and I wasn't sure about the way Java encoded text and what means Java had for writing/reading files in various encodings. 

In general I felt like I was drowning in all of the different options for reading and writing files in Java. I looked on google ' how to read a file in JAva' and the answers were terrifying. The same of writing. And the most frustrating part is that every tutorial seemed to use a different means for writing a file. Recently I came across this stackoverflow post that sort of echoed my feelings:

https://stackoverflow.com/questions/13959766/best-file-i-o-option-in-java

There are many ways to handle file i/o in Java. A rule of thumb I found in that post for handling file I/O on that page:
* Class contains 'Stream' = Handles bytes
* Class contains 'Reader/Writer' = Handles text. Usually using a default encoding.


## Reader/Writer Example



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

## War story.

I'm speaking from experience on this. When I worked for a machine learning company for a few years, we did machine learning on terabytes of social media data. We would gather text from facebook, twitter, instagram, blogs, etc, and we would categorize people based on their posts. Then we would sell this data to advertising agencies as well as other companies so they could target people with ads based on personality type. Kind of scary stuff, but the pay was good.

Before even doing any cool machine learning, we had to go through great efforts to ensure that we knew the text encodings for every piece of text we had. This was a bit of a challenging technical problem, but not too bad. If we would have run an algorithm on a mixture of utf8, utf16, and utf32 encoded text the results owuld have been garbage! 

I won't tell you the boring details of how we checked text encodings - now you know enough to write your own code to check text encodings. This information is very important for machine learning, for writing international websites, for writing apps - when you write an android app and a user copies from text from a page in your app to paste it into google - what encoding is that text? Will google recognize it? Will your browser crash?

This is important to know.
