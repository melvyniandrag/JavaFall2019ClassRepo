package textencodings;  

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ScannerExample {

	public static void main(String[] args) {
		File f = new File("src/dostoyevsky-utf8.txt");
		final String ENCODING = "UTF8";
		Scanner scanner = null;
		try{
			scanner = new Scanner(f, ENCODING);
		}
		catch ( FileNotFoundException fnf ) {
			System.err.println(fnf.getMessage());
		}
		
		while(scanner.hasNext()) {
			System.out.println(scanner.next());
		}
	}
}
