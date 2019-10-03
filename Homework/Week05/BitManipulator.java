public class BitManipulator{
    /*
     * Funky function that makes a short from 4 bytes.
     *
     * @param byteArray - byte[] of length 4.
     * @return a short made from the last 4 bits of every byte in byteArray.
     *
     */
    private static short extractShort(byte[] byteArray) throws ByteArrayLengthException {
        if( byteArray.length != 4 ){
            throw new ByteArrayLengthException("parameter byteArray to 'extractShort' must have length 4.");
        }
        else{
            // TODO implement this part.
            short s = 1;
            return s;
        }
    }

    private static byte[] intToByteArray(int i){
        //TODO convert 'i' to a byte array. Remove this 'dummy' stuff.
        byte[] dummy = { (byte)0x19, (byte)0x01, (byte)0x7A, (byte)0xAF };
        return dummy;
    }

    public static void main(String[] args){
        int i = 56401;
        byte[] byteArr = intToByteArray(i);
        try{
            short s = extractShort(byteArr);
                        System.out.println(String.valueOf(s));
        }
        catch( Exception e ){
            System.out.println(e.getMessage());
        }
    }    

}

class ByteArrayLengthException extends Exception{
    public ByteArrayLengthException( String s ){
        super(s);
    }
}

