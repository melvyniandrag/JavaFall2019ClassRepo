import java.io.*;
import java.nio.charset.Charset;

public class NoUTF16OnWindows{
	public static void main(String[] args) throws Exception{
		File file = new File("dostoyevsky-utf16.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-16")));
		try {
			String fileContent = br.readLine();
			System.out.println(fileContent);
		} finally {
			br.close();
		}
	}
}