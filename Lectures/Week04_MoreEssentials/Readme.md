# Week 04 Lecture

Last week we covered a couple of interesting topics in Java - namely, a little bit about Java packages, the basic Java number classes, and Java Strings. Tonight we will cover three more topics in Java. 

* Concurrency
* Exceptions 
* File I/O

Of course there is so much to know about these three topics, so I'll just try to give you a little morsel of information about each.

* Concurrency - Java programs can do multiple things at once. This may surprise you to find out.
* Exceptions - Sometimes things go wrong will your program is running. Java has exceptions to help handle these things.
* File I/O - So far we've only been writing out to the terminal. Java can also write to files ( and read from files too! )

## Threads + concurrency

* [BEST INTRO REFERENCE I COULD FIND] https://www.javaworld.com/article/2074217/java-101--understanding-java-threads--part-1--introducing-threads-and-runnables.html?
* [MORE TECHNICAL REF] https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html
* [A BIT TECHNICAL BUT GOOD] https://www.baeldung.com/java-concurrency

### A Real Life Story

There are many times when computers need to do multiple things at once. One
example I have extensive experience with is Bluetooth. I showed you on day one
of this class a little app I made that rolled a ball on the screen of a tablet
based on bluetooth messages coming from another smartphone. When I move one
smartphone, the ball on the other smartphone moves. 

On the smartphone with the moving ball, the phone needs to simultaneously draw
the graphics on the screen of the moving ball, as well as listen for
instructoins from the remote telephone. All of this happens simultaneously. How?
Threads.

Another example is a radio controlled car. The computer on the radio controlled
car will be simultaneously sending instructions to the motors on the wheels to
turn left, right, go straight, stop, etc, but also, it needs to listen to
instructions coming in from the controller - be it over bluetooth or some other 
radio set of radio waves. One way to do that is with threads.

So when you hear people talk about 'concurrency' this is what they mean. There
are different ways to achieve concurrent execution of code on a computer, but a
popular way is through threads. Now I will show you how to make your Java
programs do multiple things at once.

I'm going to follow the "BEST INTRO REFERENCE I COULD FIND" because it is a
great write up.

### How to do it

See `Code/ThreadDemo.java`

Compile and run it. Notice that there are println() statements both in the run()
and main() methods. We should see the outputs mixed up, since these are running
at the same time.

Depending on how our operating systems work, we may or may not see the print
statements mixed up in the output.

( on my t450 I keep getting the stars to print out before the "i=" statements
print. Increase the bounds of the for loops until we see a mixture. Someone's
computer ought to mix up the print statements

When you compile and run a Java program, it runs on a main thread. This is the
code executed in the main() method. But you could also create new Threads that
run alongside it. Just like in this example. We create a new class called
"MyThread" that extends the "Thread" class ( The Thread class is provided by
Java, you don't need to write it. ) This class has a run() method that runs
alongside your main() code.

ALSO note the new for loop syntax. Not relevant to threads, just note that you
can increment two things in a for loop and not just one.

`for(int count=1, row=1; row<20; row++, count++)`


See `Code/NameThatThread`. 

Compile and run the code as

`java NameThatThread myNameIs`

and 

`java NameThatThread`

The take aways from this example are 

* classes that extend Thread have a getName() method that gets the name of the
  thread. 
* Java classes can have multiple constructors. So far we only wrote code that
  has one constructor. Note that MyThread has two - one that takes no params,
and one that takes a String.
* We finally use the `String[] args` from `main(String[] args)`!

Just like last time we:
* Create a class that extends `Thread`
* Run the thread by using `.start()`

But now, what is new to us, and is an essential part of Java  (not related to
Threading, just related to Java) is that we can supply command line arguments to
our code! The arguments we supply to our code go into the `String[] args` array.

I'm going to show you that we can supply multiple command line arguments to a
Java program.

See `Code/CommandLineArgs`

Run the following:

```
melvyn@machine$ java CommandLineArgs
#no output
melvyn@machine$ java CommandLineArgs one 2 three 4 e f g
args[0]=one
args[1]=2
args[2]=three
ETC
```

### Pop Quiz
What is the type of '2' in the code? Is it an int, short, float or String?

Answer - a String. Command line arguments are Strings and are stored in a String
array ( String[] args )


### Returning to threading.
Now I'm going to extend the NameThatThread example to spawn a bunch of threads
based on command line arguments

See `Code/NameThoseThreads.java` Note how we can name the different threads
using command line arguments

In this example you learned a few concepts in only one short example! You
learned command line arguments, Threads, and multiple constructors per class!

### Exercise1 with multithreading
Extend the `NameThoseThreads` example such that the run() method of each prints 

`My name is : [THREAD NAME] My iter is [ITER NAME]`

and have the for loop in run() that iterates 100 times.

You should see something like e.g.

```
melvyn@machine$ java NameThoseThreads sheep horse pig
My Name is : sheep My iter is 0
My Name is : sheep My iter is 1
My Name is : sheep My iter is 2
My Name is : pig My iter is 0
My Name is : horse My iter is 0
My Name is : sheep My iter is 3
```

### Exercise 2 with multithreading

Change the `NameThoseThreads` example such that the `MyThread()`  constructor
that doesn't take a String parameter will name the thread "DEFAULT" by default.
Then change the line

`mt3 = new MyThread(args[2])`

to 


`mt3 = new MyThread()`


Verify that it no longer says 'pig' in the output, but now says 'DEFAULT'.


### Exercise 3 with multithreading

Change the calls to `super(String s)` to `setName( String s)`. `super()` calls
the parent class constructor ( in this case the Thread constructor ) and that
has the effect of setting the name of the Thread. You can achieve the same end
with a call to `setName()`. Nothing fancy, just a thing you can do. If you ever
need to name a thread for some reason or other, just be aware that you have two
options for doing it.

### Returning to lecture
So now you've compiled some multithreaded code. I expect it to still be
confusing. You probably have no idea what just happened - that's fine, we're
going to talk a bit more about it, and in the end it will go getting clearer and
clearer until it clicks.

### Calculating PI.

Keep in mind, all the examples I'm showing you are lifted from the resource
linked above. 

In this example we are going to introduce the 'Thread.sleep(long millis)`
method. You can pause the execution of a thread for a certain number of
milliseconds by using `Thread.sleep()`.

We will illustrate this by computing the number pi.

This algorithm is the one described in detail here:
http://mathworld.wolfram.com/PiFormulas.html

Pretty cool algorithm if you're a mathy type - you can sum a bunch of carefully
chosen fractions and get an increasingly accurate representation of the number
Pi.

See `Code/CalcPI1.java`

Compile and run the code. Depending on your machine you'll either get 3.14, or
else you'll get a number that looks nothing like Pi. ( let everyone run the same
code. Note that everyone's answer is different. Find at least one person with a
bad answer ).

The idea is that we want the mt.run() method to finish before we print 'pi =
WHATEVER'. The Sleep here causes the main() thread to sleep for 10
milliseconds). That may not be enough time for the pi calculation to finish,
because remember, they are running at the same time.

How to make main() wait longer to ensure we get the right answer? 

Play around with the Thread.sleep(). increase the sleep time until we get the
right answer. Do a doodle on the board showing the two vertical lines
illustrating thread execution, like this one:

http://math.hws.edu/javanotes/c12/s1.html ( scroll down a bit )


### Exercise With Sleep

Add a sleep to the run() method. This will make your MyThread instance sleep,
while main() rushes ahead. Verify that you can mess up the calculation a


### Note about the exercise
Ignore that it says try/catch. I'm going to teach you that but I want you to
have seen them a few times before I explain them. In an hour or so I'll tell you
what that is.


### Improving upon CalcPi1.java

This example introduces the `isAlive()` method. 

See `Code/CalcPI2.java`

Now the while loop in `main` runs so long as your MyThread instance is running.
So, now the main() method won't print the value of pi until MyThread has
finished calculating it, and ended ( died ).

### Improving on CalcPI2.java

See `Code/CalcPI3.java`

We introduce one more thing - `.join()`. This is a way to have your spawned
thread tell the calling thread that it is done. 

`.join()` will wait until the thread's run() method completes and then continue
execution. Open discussion for a bit.

### Exercise
Quickly create two files. 

* Main.java
* JavaThread.java


In Main, create and `.start()` an instance of `JavaThread`.

Make the JavaThread's run() method print out:

```
THREAD_NAME - 1
THREAD_NAME - 2
...
THREAD_NAME - 50
```

Then `.join()` the `JavaThread` and Print out 'Done Printing  1 - 50' .

### Daemon Threads vs. User Threads

There are two types of threads in the world. There may be more, but I know of
two. There are *daemon* ( say demon, not daymon ) threads and *user* threads. 
So far we have written user threads. User threads are threads whose run() method
must finish before the program ends.  Daemon threads will run in the background
of your application, but when main() exits, the daemon thread ends too, even if
it wasn't done doing it's stuff.

See `Code/UserDaemonThreadDemo.java`

Note the call to `setDaemon`. If you don't provide a command line agument, the
thread is started as a user thread. The program will run and not stop.

If you do provide a command line argument, the thread will be a daemon and the
program will end, even though the daemon thread want's to run forever (
while(true); )


### Garbage Collection as a daemon thread

Explain how the java garbage collector runs on a daemon thread briefly. Probably
no one will understand, but at least they'll have heard me talk about garbage
collection for a few minutes so the next time they hear the expression they know
it's important.

### Exercise

Change the UserDaemonThreadDemo code such that it checks if the command line
argument is t, f, or otherwise. If it gets 't', it makes the thread a daemon. If
it gets an 'f' , the thread is not a daemon. If it gets a number of args != 1,
report an error and return. If it gets an arg that is neither t nor f, report an
error and return. Verify that the thread behaves as expected.


### Recap and what's next

Let's recap what we learned so far:

* You can extend the Thread class to make a class with a run() method.
* You start your thread with .start()
* You can use the .isAlive() and .join() methods to wait for the thread to
  finish
* The thread can be either a User thread or a daemon thread.

Make sure you understand these points. I'm going to tell you one more thing
about multithreading in Java, and this deals with 'Runnables'

### Preamble to Runnables

*JAVA FACT! - You can only extend one class at a time*

In Java you cannot do this:

or 

```
public class MyClass extends BaseClass1 and also extends BaseClass2{
}
```

There is no syntax for this. In Java you can only extend one class. There is no
"Multiple Inheritance". There is no "Multiple inhgeritance" There is no
"Multiple Inhgeritance", say this to your self a bunch of times.

A Java class CAN implement multiple interfaces, but it cannot extend multiple
classes.


### Exercise together

Write 5 java files:

* Main.java
* B.java
* A.java
* IFace1.java
* IFace2.java

Make B extend A, make A implement IFace1 and IFace2.

Add a method with a for loop to B that prints numbers 1 - 10.

How do we get B to run that method in a thread?

Solution 1 - We make B extend not only A, but also extend Thread, so that we can
call the run() method from Thread. 

Why can't we do this???!?! Can we extend 2 classes at once in Java? What is the
mantra again?

So this is why we need Runnables.

You can do run threads by extending the Thread class, or by implementing the
Runnable interface and passing your runnable object to the Thread constructor.

See `Code/Runnables/*`

### Why do I even care about concurrent programming?!?!
I already gave you one example, but I can give you another one. Let's do an
exercise

### Exercise
Use a combination of google and whatever computer tricks you know to find out
what processor is in the computer you are on right now. It might be an Intel i5,
i3, i7, maybe a Ryzen processor, maybe an arm 64 bit processor of some sort,
just use google to figure out which processor you have.

Then find out how many cores are in your computer. 

=== Wait until everyone knows, might take 5 - 10 mins ===

You will all see that your processors, regardless of the manufacturer ( intel,
amd, etc ), have more than one core. Each core is a processor. So your computer
hardware is built to do several things at once. You need to learn to write
concurrent, multithreaded code, so that you can use your full processor. 

This is why I always complain about people wanting to upgrade to the latest
macbook pro - many of those folks have no use buying the latest 6 core
processor, because they are probably still writing single threaded code that
would run just as well on a single core machine. You need to learn to write fast
multithreaded programs, because the present and future of computing is
concurrent. Single threaded programs are a thing of the past. Your computer is
built for multitasking - learn how to take advantage of that!

Salaries are better for quality concurrent programmers too.

Go on amazon and search for concurrent programming. Show how many huge texts
there are about this. I only showed a couple of key words, hope to have piqued
your curiosity so you go read one of these books and learn more.

## Exceptions



## Exercise with Exceptions


## File I/O

*Reference https://www.geeksforgeeks.org/file-class-in-java/*

## [ 9:35 - 9:42 ] Exercise with File I/O


