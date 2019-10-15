import java.io.*;
public class Main{
	public static void main(String[] args){
		String fileName = args[0];
		try{
			int[][] imageArr = MessageHider.imageTo2DArr(fileName);
			byte[] messageBytes = args[1].getBytes("UTF-16");
			System.out.println("Attempting to hide bytes: ");
			for( byte b : messageBytes ){
				System.out.print(String.format("%02X ", b));
			}
			System.out.println("\nHidden bytes are:");
			MessageHider.HideBytes(messageBytes, imageArr);
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

