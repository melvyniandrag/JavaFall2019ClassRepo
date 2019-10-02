public class UnsignedShift{
	public static void main(String[] args){
		int i = -1; // aka 0xFF 0xFF 0xFF 0xFF. 
		int iUShift = ( i >>> 1 ); // aka 0x7F 0xFF 0xFF 0xFF ( note the the leading byte is now a 0 ( b01111111 = 0x7F )
		int iSShift = ( i >> 1 ); // aka 0xFF 0xFF 0xFF 0xFF
		System.out.println(String.valueOf(iUShift));
		System.out.println(String.valueOf(iSShift));
		// to save time we won't verify the value of iUShift. 
		// check here:
		// https://www.rapidtables.com/convert/number/hex-to-decimal.html
		// and put in "7FFFFFFF" as the hex value. Check out the decimal value that comes out.
	}
}
