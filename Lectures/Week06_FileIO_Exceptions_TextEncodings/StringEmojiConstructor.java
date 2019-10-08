public class StringEmojiConstructor{
	public static void main(String[] args){
		String s = "😂";
		int emoji = Character.codePointAt(s, 0);
		String unumber = "U+" + Integer.toHexString(emoji).toUpperCase();
		System.out.println(s + "  is code point " + unumber);
		String s2 = new String(new int[] { emoji }, 0, 1);
		System.out.println("Code point " + unumber + " converted back to string: " + s2);
		System.out.println("Successful round-trip? " + s.equals(s2));
	}
}

