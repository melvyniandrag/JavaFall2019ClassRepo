public class JavaEndianness{
    public static byte[] intToBytes(long l) {
        byte[] result = new byte[4];
        for (int i = 3; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }
    
    public static void main(String[] args){
        int i = 1;
	byte[] bytes = intToBytes(i);
	for( byte b : bytes ){
		System.out.print( String.format("0x%02X ", b) );
	}
	System.out.print(String.format("%n"));
    }
}
