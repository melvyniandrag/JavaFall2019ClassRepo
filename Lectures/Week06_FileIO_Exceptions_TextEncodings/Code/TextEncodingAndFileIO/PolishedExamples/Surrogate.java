/*
 * Got the String(char[] charArr, int offset, int count) constructor from the 
 * documentation here: https://docs.oracle.com/javase/7/docs/api/java/lang/String.html
 * Got the surrogate pair for the treble clef from here:
 * http://www.informit.com/articles/article.aspx?p=2274038&seqNum=10
 * more info about the treble clef:
 * https://www.compart.com/en/unicode/U+1D11E
 */

public class Surrogate{
	public static void main(String[] args){
		char[] surrogatePair = {'\ud834', '\udd1e'}; // Pair of UTF-16 encoded characters.
		// Note, if you want, that neither \uD834 nor \uDD1E are valid characters by themselves.
		String s = new String(surrogatePair, 0, 2);
		System.out.println(s);

		// another way to do this. 
		// In Java, you can create Strings from the decimal representation of
		// the Unicode code point.
		// The treble clef is Unicode code point U+1D11E. Do not confuse this with the hex representation in UTF16 
		// the UTF 16 representation is the one above. The Unicode code point is the representation of the code point
		// in ANY UTF-scheme.
		int trebleClefInt = 0x1D11E;
		String s2 = new String(new int[]{trebleClefInt}, 0, 1);
		System.out.println(s2);
	}
}

