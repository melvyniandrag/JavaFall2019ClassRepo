# Week 04 Lecture

## A review of the terminal

* Look at the cd command
* DIR vs ls
* What is git bash?

## Homework review.
Quickly go over homework? Give another few days to complete?


## Lecture Intro

Computers are tools for doing cool stuff. You can do math, make music, drive robots, make mobile apps, write games, etc.. A hobby of mine is playing the guitar and I found that Java was useful for me to verify some cool mathematical properties of the guitar. I'm going to share some of the stuff I did in Java that are relevant to music, I hope you find it interesting. 


## About music

### What is a note

If you are a musician, some of what I say here may be imprecise, but I hope you'll agree it's essentially true, though maybe I'm leaving out some details.

A note is a sound made on an instrument. The sound is identifiable by the way it makes the molecules move in the medium it moves through. Ultimately it causes your eardrum to vibrate in a particular way and that you can identify as a particular note. 

In the western musical tradition there are 12 notes that are repeated across octaves. I think the classification of notes is different in different cultures, though I'm not sure. And, it's not relevant to our discussion today, though if you are a music major feel free to tell me what you know after class as I'm very curious.

The notes we have in Western Music are : A B C D E F G. That is only seven - remember I told you that there are 12. Now I'm going to tell you something that I don't quite understand. I don't understand why the musicians did it this way, but they did. What they did is that between some notes they put a sharp, and between other notes they did not. 

A A# B C C# D D# E F F# G G# ( and then back to A )

Notice that between the B,C and E,F there is no sharp note. In case you think I'm lying to you, you can just look at a piano and see that what I've said is true

### Exercise for the non piano players:
The sharps are the black keys. The regular notes are the white keys. Show a picture of a piano. Bonus points for the first student to tell me where a B is on the piano.

Keep in mind that the patterns repeat across octaves on the keys. So notice the recurring pattern of whites and blacks as we move down the keyboard.

### Notes on a guitar
* Show the notes on a guitar

* Show how the same note appears in several places along the guitar neck.  Play the string so people can hear the sound in different places.

### What is a chord.
A chord is a bunch of notes that sound good when played simultaneously. Any musicians here? Are a bunch of dissonant notes referred to as a chord? There is alot to know about music and this class isn't about music, it's about Java, I'm just giving a tiny bit of info to you so you can complete and assignment. So I don't want to talk about general chords - we will focus on a particular chord called a 'triad', which is extremely common in music. I've been trying to play guitar with little discipline for a few years, and the only chords I see people talk about are triads. 

Now this is important to the lesson.

To get a triad you do this ( again correct me if I'm wrong ):

* Choose a 'root note' - add this to your chord
* Walk up the note list 4 steps - add this to your chord
* Walk up the note list 3 more steps - add this to your chord

and we are done - we have made a triad.

For example, a C Chord is made of the set `{C, E, G}`

And look how we get it.


1. Start at the root note 'C' : 
```
A A# B C C# D D# E F F# G G# A A#
       ^
```

2. Then go forward 4 steps:
```
A A# B C C# D D# E F F# G G# A A#
       ^         ^
```

3.  Then go forward 3 steps
```
A A# B C C# D D# E F F# G G# A A#
       ^         ^      ^
```

If you don't like this, don't blame me! This is just the way it's done.

### Chords on the guitar
Show how strings are laid out on the guitar, and label the notes on the first few frets on the guitar on the blackboard.

Show how the same chord can be played in various spots on the guitar by identifying the notes one at a time on the neck.

### Exercise
Verify I am not making a mistake. Take the list of notes and try to construct various chords from it as is done on the wikipedia triad page.
Verify that wikipedia is right and that what I have said is right.
Take 2 minutes. All you have to do is count and point at a number line!


And so if we can all agree to these basic rules, we can move on to do something interesting with the Guitar, and with Java

## The CAGED system

So by know I've tried to make it approximately clear that a guitar can play the same chord in multiple spots. Anyone want to try? [ let someone try ]

In fact, there is a system to remember where some of these chords appear so you can quickly find a chord. The System is called the 'CAGED' system and it provides you with a super simple trick to remember where a chord is on the guitar. 

The CAGED system uses 5 simple chord shapes that work up and down the guitar neck. Since the shapes are simple and repetitive, analysis of the CAGED system lends itself to computer analysis! And that's what we're going to do.

## The C shape
Lecture this one

Root on 5th string

The c shape results from putting your fingers on the guitar in the following way:

E x
A 3 ( C )
D 2 ( E )
G 0 ( G )
B 1 ( C )
E 0 ( E )

C C# D D# E F F# G G# A A# B C
^         ^      ^ 

## The A shape

Lecture this one

Root on 5th string

The A shape - D shape will be done in class.

The A shape looks like this:

E x
A 0 (A)
D 2 (E)
G 2 (A)
B 2 (C#)
E 0 (E)

A A# B C C#  D D# E F F# G G# A
^        ^        ^

## The G shape
In class exercise

Root on 6th string

E ( 3 ) G
A ( 2 ) B
D ( 0 ) Dd
G ( 0 ) G
B ( 0 ) B ( but some people put the finger on fret 3, giving a D )
E ( 3 ) G

G G# A A# B C C# D D# E F G G#
^         ^      ^

## The E shape


In class exercise

Root on 6th string

## The D shape

Implement the D shape for homework

The D shape


## Sorting Arrays and Other Array Operations

I showed you how to import some custom packages last time. You can also import some standard Java libraries, in case you didn't know. These are there because the java compiler knows where they are.

Show example of sorting Java array.

Show a few other array operations

Show how to import some other couple of things.

Reference:
https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html

* hashcode( ArrType[] a ) - returns int
* fill( ArrType[] a, ArrType elem ) - fills in place
* sort(ArrType[] a) - sorts in place
* copyOf( ArrType[] a, int newLength); returns array of type ArrType and length newLength. Truncates 'a' or pads with zeros based on newLength; What happens if you put a negative newLength?

See `Code/Arrays`

## File I/O in Java

How to write Strings to file.

Show the Character set on your computer.

The character set on your computer tells you which bits your computer uses to represent certain strings. You can see what character set your computer uses. 

In a few weeks I'm going to give you a lecture on the details of character sets and to see how they are handled in various operating systems, environments, etc..

See `Code/Charset`

and 

See `Code/FileIO`


## Singleton classes
Write some Singletons.
Bad Java practice, but you should know about it because it is out there.

See `Code/SingletonExample.java`

Discussion of getInstance() - the Singleton design pattern / antipattern is such that there is only one instance of an object in existence at a time.

## pop quiz
* What is a character set
* What is a singleton

## Exploring the Java compiler

### class path
* Learn about classpath

How to set it - pass the -classpath or -cp flag to javac / java. By default it looks in the pwd, but we can put the classes other places
What does it do?

### jar files
jar files are a convenient way of sharing java code. You can use jars instead of zips when you want to send a bunch of java code to someone, for use maybe as a library.

Reference about jars
https://docs.oracle.com/javase/tutorial/deployment/jar/basicsindex.html

Focus on
* creating
* view contents
* Run a jar application.
* manifest file

