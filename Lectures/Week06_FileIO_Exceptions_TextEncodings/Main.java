import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
//w w w. j ava 2s . c o m
public class Main {

  public static void main(final String[] args) throws IOException {
    File infile = new File("dostoyevsky-utf16.txt");
    FileInputStream inputStream = new FileInputStream(infile);
    Reader in = new InputStreamReader(inputStream, "UTF-16LE");
    int read;
    while ((read = in.read()) != -1) {
      System.out.print(Character.toChars(read));
    }
    in.close();
  }
}
