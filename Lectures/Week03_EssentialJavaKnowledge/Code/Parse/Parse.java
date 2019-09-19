/*
 * These few method calls are just illustrations of the 
 * Byte.parseByte(String string, int radix)
 * String.valueOf(byte b)
 * String.valueOf(int i)
 * and
 * Integer.parseInt(String s, int radix)
 *
 * methods shown here:
 * https://docs.oracle.com/javase/tutorial/java/data/numberclasses.html
 */


import java.lang.*;

class Parse{
	public static void main(String[] args){
		//String s = "300"; // Doesnt work
		String s = "127"; // works. Do you know why?
		int ten = 10;
		byte b = Byte.parseByte(s, ten);
		System.out.println(String.valueOf(b));



		int hex = 16;
		String sHex = "10"; // should be 16 in decimal.
		int  i = Integer.parseInt(sHex, hex);
		System.out.println(sHex + " in hex, converted to decimal = " + String.valueOf(i));

	}
}




