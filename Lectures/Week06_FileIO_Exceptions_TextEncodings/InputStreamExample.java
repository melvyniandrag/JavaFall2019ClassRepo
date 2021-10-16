package textencodings;

import java.io.*;

public class InputStreamExample{
	public static void main(String[] args) throws Exception{
		File file = new File("src/dostoyevsky-utf8.txt");
		BufferedReader br = new BufferedReader(
					new InputStreamReader( new FileInputStream(file), "UTF8")
				);
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        line = br.readLine();
		    }
		    System.out.println(sb.toString());
		} 
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}
		finally {
		     br.close();
		}
	}
}
