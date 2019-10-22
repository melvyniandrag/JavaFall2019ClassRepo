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
		ChangeBackground.WritePPMFile(intArr, outputFile);
	}
}

