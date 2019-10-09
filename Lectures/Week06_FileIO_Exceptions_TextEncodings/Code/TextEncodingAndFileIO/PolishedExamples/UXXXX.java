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

public class UXXXX{
	public static void printBytes(byte[] byteArr){
		for ( byte b : byteArr ){
			System.out.print(String.format("%02X ", b));
		}
		System.out.println("");
	}
	
	public static void printSeparator(){
		System.out.println("************************************");
	}
	public static void main(String[] args){
		String Zero = "\u0030";
		printSeparator();
		System.out.println(Zero);
		byte[] zeroBytes = Zero.getBytes(Charset.forName("UTF-16"));
		printBytes(zeroBytes);

		printSeparator();
		String SymbolIDontRecognize = "\u0080";
		byte[] symbolBytes = SymbolIDontRecognize.getBytes(Charset.forName("UTF-16"));
		System.out.println("\u0080");
		printBytes(symbolBytes);
		// Note that \u0080 should be a "€".
		// But when we print this in the terminal on my computer we don't see that character.
		// Look here:
		// https://www.utf8-chartable.de/unicode-utf8-table.pl
		// This is because my computer prints out Strings in utf-8. 
		// As shown in the link above, the utf-8 encoding for \u0080 is \xc2\x80
		// but the font package I have installed on my computer does not print that value
		// So we just see a 'missingno' like in pokemon.
	}
}

