import java.io.*;
public class Main{
	public static void main(String[] args){
		String fileName = args[0];
		try{
			int[][] imageArr = MessageHider.imageTo2DArr(fileName);
			MessageHider.writeImage(imageArr);
		}
		catch(NoAlphaChannelException ex){
			System.err.println("File doesn't have an alpha channel: " + fileName);
		}
		catch(IOException ex){
			System.err.println("Error opening file " + fileName);
		}
	}
}

