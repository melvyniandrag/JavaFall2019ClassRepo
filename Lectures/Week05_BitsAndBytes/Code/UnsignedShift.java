public class UnsignedShift{
	public static void main(String[] args){
		byte i = -1; // aka 0xFF 0xFF 0xFF 0xFF. 
		byte iUShift = (byte) ( i >>> 1 ); // aka 0x7F 0xFF 0xFF 0xFF ( note the the leading byte is now a 0 ( b01111111 = 0x7F )
		System.out.println(String.valueOf(iUShift));
	}
}
