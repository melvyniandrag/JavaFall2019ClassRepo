import java.nio.charset.Charset;

public class CharacterToChars{
	public static void main(String[] args){
		final char[] chars = Character.toChars(0x1F600);
		final String s = new String(chars);
		final byte[] asBytes = s.getBytes(Charset.forName("UTF-8"));
		System.out.println(s);
		System.out.println(asBytes.length);
	}
}
