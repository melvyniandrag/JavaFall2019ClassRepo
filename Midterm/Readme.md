## TL;DR. Concise Exam Problem Statement
Attached is a 16 pixel ppm image. Hide the ASCII encoded message "Hi" in the last bit of the red channel of the image.
Then extract the ASCII message to verify your devious scheme actually worked.

## Details About PPM Pixels
I chose PPM format for this exam because, while it is not a commonly used image exchange format, it is absolutely the easiest image format to understand. The image file is written in text!

The PPM format stores the pixel values as a triplet of whitespace separated numbers in the range ( 0 - 255 ).
In the red image you will see:

```
255 0 0
```

That means that the pixel has a red value of 255, a green value of 0 and a blue value of 0. 255 is written in unsigned binary as 11111111 - note that depending on how you write your code you may/may not have trouble with this as Java numbers are signed. Be careful!

If you wanted to hide a zero bit in the last bit of this byte you would change `11111111` to `11111110`. If you wanted to hide a one bit in this byte, you would do nothing. Leave `11111111` as `11111111`.


### Details about the PPM header
Ignore the header

```
P3
4 4
255
```

unless your curiosity is killing you and you want to read more, in which case, go ahead. P3 means something, as does 255. The 4 and 4 indicate the dimensions of the image.

## Submission
Due via Blackboard by 11:59 PM on October 22nd.

For simplicity, I am providing you this starter code. Your `MessageHider` class must provide an API that allows it to satisfy this code.

```
public class Exam{
	public static byte[] StringToBytes(String Message){
		return new byte[]{0x00, 0x00}; // Fix this.
	}

	public static void main(String[] args){
		final String InputFilename = args[0];
		final String OutputFilename = args[1];
		final String Message = args[2];
		final byte[] MessageAsBytes = StringToBytes(Message); // Make sure to get the bytes in the proper text encoding!
		if( MessageAsBytes.length != 2 ){
			System.err.println(("You cannot hide the string %s with this code as it doesn't satisfy the string length req't",Message));
		}	    	
		MessageHider.hideMessage( inputFilename, outputFilename, MessageAsBytes);
		final String extractedMessage = MessageHider.extractMessage(outputFilename);
		System.out.println(String.format("Found message '%s' in %s", extractedMessage, outputFileName));
	}
}
```

* 25 points for successfully hiding a message. 
* 25 points for successfully extracting a message.
* 50 points for successfully explaining your code in class.

I will verify that your code works by providing different inputs to the code and making sure it works for all of them.

And, as there is no way for me to prevent one person doing all the work and simply distributing copies of the exam to others, I am going to test you on the code you wrote. I will likely print out your submission and have you write comments on it explaining how everything works.

Grading this part is subjective and I reserve the right to administer points as I deem fair. 

The take away is - do whatever you have to do to make code that works. Work together, copy from various internet sites, etc.. BUT be prepared to defend every line of code you write.
