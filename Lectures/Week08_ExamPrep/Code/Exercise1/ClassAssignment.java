import java.io.*;
public class ClassAssignment{
	public static void main(String[] args){
		final String inputFile = args[0];
		final String outputFile = args[1];
		int[][] intArr = null;
		try{ 
			intArr = ChangeBackground.ReadPPMIntoIntArray(inputFile);
		}
		catch( FileNotFoundException ex1 ){
			ex1.printStackTrace();
		}
		catch( IOException ex2 ){
			ex2.printStackTrace();
		}

		ChangeBackground.ChangeWhiteToRed(intArr);
		try{
			ChangeBackground.WritePPMFile(intArr, outputFile);
		}
		catch( FileNotFoundException ex2 ){
			ex2.printStackTrace();
		}
		catch(IOException ex1){
			ex1.printStackTrace();	
		}

	}
}

