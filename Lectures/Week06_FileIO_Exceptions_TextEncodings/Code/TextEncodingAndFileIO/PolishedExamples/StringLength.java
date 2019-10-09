import java.nio.charset.Charset;

public class StringLength{
	public static void printBytes(byte[] byteArr){
		for ( byte b : byteArr ){
			System.out.print(String.format("%02X ", b));
		}
		System.out.println("");
	}
	
	public static void main(String[] args){
		String fyodor = "Фёдор";
		System.out.println("Length of string " + fyodor  + " = " +  String.valueOf(fyodor.length()));
		byte[] fyodorBytes = fyodor.getBytes(Charset.forName("UTF-16"));
		System.out.println("Number of bytes in " + fyodor + " = " + fyodorBytes.length);
	   	printBytes(fyodorBytes);
	
		String hello = "hello";
		System.out.println("Length of string: " + hello + " = " + String.valueOf(hello.length()));
		byte[] helloBytes = hello.getBytes(Charset.forName("UTF-16"));
		System.out.println("Number of bytes in " + hello + " = " + helloBytes.length);
		printBytes(helloBytes);
	}
}
