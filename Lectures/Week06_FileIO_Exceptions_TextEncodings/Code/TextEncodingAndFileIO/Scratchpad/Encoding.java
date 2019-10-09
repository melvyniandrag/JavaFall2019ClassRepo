import java.nio.charset.Charset;

public class Encoding{
	public static void main(String[] args){
		Charset defaultCharset = Charset.defaultCharset();
		System.out.println(defaultCharset.toString());
	}
}
