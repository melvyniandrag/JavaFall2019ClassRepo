package textencodings;

import java.io.UnsupportedEncodingException;

public class UTF16Lengths {

	public static void main(String[] args) {
		String hello = "hello";
		System.out.println("Number of characters in string: " + hello.length());
		byte[] helloBytes = null;
		try{
			helloBytes = hello.getBytes("UTF16");
		}
		catch(UnsupportedEncodingException uee) {
			System.err.println(uee.getMessage());
		}
		if(helloBytes != null) {
			System.out.println("Number of bytes in byte array: " + helloBytes.length);
		}
	}
}
