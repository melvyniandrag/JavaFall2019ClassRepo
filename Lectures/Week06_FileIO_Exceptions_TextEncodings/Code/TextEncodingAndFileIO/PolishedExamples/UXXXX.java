/*
 * The utf 16 representation of a code point that can be represented in 2 bytes is just the hex value 
 * of the code point. 
 * So, the utf code point for this symbol: €
 * which is U+0080, is just two bytes : 0x00 and 0x80.
 * Notice that UTF-16 code points that require 4 bytes, or 2 2 byte characters, do not follow this scheme.
 * see the Surrogate.java example, where you see that U+1D11E is not stored in the computer as 
 * 0x00 0x01 0xD1, 0x1E. Instead, it used two surrogates with different numeric values.
 */
public class UXXXX{
	public static void main(String[] args){
		System.out.println("\u0030");
		// Note that \u0080 should be a "€".
		// But when we print this in the terminal on my computer we don't see that character.
		// Look here:
		// https://www.utf8-chartable.de/unicode-utf8-table.pl
		// This is because my computer prints out Strings in utf-8. 
		// As shown in the link above, the utf-8 encoding for \u0080 is \xc2\x80
		// but the font package I have installed on my computer does not print that value
		// So we just see a 'missingno' like in pokemon.
		System.out.println("\u0080");
	}
}

