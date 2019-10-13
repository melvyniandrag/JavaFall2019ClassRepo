/*
 * See:
 * https://www.fileformat.info/info/unicode/char/1f600/index.htm
 */
public class PrintEmoji{
	public static void main(String[] args){
		String smiley1 = "\ud83d\ude00";
		System.out.println(smiley1);
		String smiley2 = new String(new int[]{ 0x1f601 }, 0, 1);
		System.out.println(smiley2);
		String smiley3 = "😀";
		System.out.println(smiley3);
	}
}
