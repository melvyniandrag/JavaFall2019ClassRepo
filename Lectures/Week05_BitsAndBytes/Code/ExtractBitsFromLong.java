/*
 * Note - I extracted the longToBytes() and bytesToLong() methods
 * from here:
 * https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
 * I looked around and this seemed like one of the better ways to do it.
 */
public class ExtractBitsFromLong{
    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }
    
    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }


    public static byte funkyFunction(byte[] bArr) throws InvalidArrLengthException{
        if( bArr.length != 8 ){
            throw new InvalidArrLengthException("Argument to funkyFunction() needs to have length 8!");
        }
        byte retVal = (byte) 0x00;
        for( int byteIdx = 0; byteIdx < 8; byteIdx++ ){
            byte tmp = (byte)( bArr[byteIdx] & 0x01 );
            byte shiftedTmp = (byte) ( tmp << (7 - byteIdx ) );
            retVal |= shiftedTmp;
        }
        return retVal;
    }

    public static void main(String[] args){
        long l = 1000000; 
	    //long l = 65438; // b 11111111 10011110 therefore, we expect to get 00000010 as our output byte.
        //long l = -1; // 0xFF 0xFF 0xFF 0xFF 0xFF 0xFF 0xFF 0xFF, we expect 0xFF as our output byte.
        //long l = 999999295438L;// Note the "L" suffix - without this, java will report an error.
			       // b11101000
                               //  11010100
                               //  10011010
                               //  01001111
                               //  11001110
			       // There expect 00000010 = 2. 
        byte[] bArr = longToBytes(l);
        try{
            byte b = funkyFunction(bArr);
            System.out.println(String.valueOf(b));
        }
        catch( Exception e ){
            System.err.println(e.getMessage());
        }
    }
}

class InvalidArrLengthException extends Exception{
    public InvalidArrLengthException(String s){
        super(s);    
    }
}
