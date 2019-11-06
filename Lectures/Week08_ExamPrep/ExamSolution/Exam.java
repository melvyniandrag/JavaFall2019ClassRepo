public class Exam{

	public static void main(String[] args){
		final String InputFilename = args[0];
		final String OutputFilename = args[1];
		final byte[] MessageAsBytes = { (byte)0x01, (byte) 0x02};
		if( MessageAsBytes.length != 2 ){
			System.err.println(String.format("You cannot hide the message %s\n", "hi"));
		}	    	
		MessageHider.hideMessage( inputFilename, outputFilename, MessageAsBytes);
		//final String extractedMessage = MessageHider.extractMessage(outputFilename);
		//System.out.println(String.format("Found message '%s' in %s", extractedMessage, outputFileName));
	}
}
