import java.io.*;
import java.nio.charset.Charset;

public class UTF16ErrorWithoutFileIO{ 
	public static void main(String[] args) throws Exception{
		byte[] fyodorUTF16Bytes = { 
					(byte)0x04, (byte)0x24,
					(byte)0x04, (byte)0x51,
					(byte)0x04, (byte)0x34,
					(byte)0x04, (byte)0x3e,
					(byte)0x04, (byte)0x40,
					(byte)0x00, (byte)0x0a
		};
		
		String fyodor = new String(fyodorUTF16Bytes, Charset.forName("UTF-16"));
		System.out.println(fyodor);
		
	}
}
