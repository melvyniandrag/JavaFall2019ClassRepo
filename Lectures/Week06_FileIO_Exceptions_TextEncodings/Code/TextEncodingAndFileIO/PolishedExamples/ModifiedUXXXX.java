/*
 * The utf 16 representation of a code point that can be represented in 2 bytes is just the hex value 
 * of the code point. 
 * So, the utf code point for this symbol: €
 * which is U+0080, is just two bytes : 0x00 and 0x80.
 * Notice that UTF-16 code points that require 4 bytes, or 2 2 byte characters, do not follow this scheme.
 * see the Surrogate.java example, where you see that U+1D11E is not stored in the computer as 
 * 0x00 0x01 0xD1, 0x1E. Instead, it used two surrogates with different numeric values.
 */
import java.nio.charset.Charset;

public class ModifiedUXXXX{
	public static void main(String[] args){
		System.out.print("\u0080");
	}
}

