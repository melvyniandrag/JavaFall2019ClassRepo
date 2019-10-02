public class BitwiseOperations{

	public static void testAnd(){
		byte b = (byte) 0xFF; // aka -1
		byte shouldBe15 = (byte)(b & 0x0F);
		System.out.println(String.valueOf(shouldBe15));
	}

	public static void testOr(){
		byte b = (byte) 0xA4; // A = 10, 4 = 4 => 0xA4 = b10100100
		byte b2 = (byte) 0xA0; // b10100000
		// expect to get 0xA4 out
		byte result = (byte)( b | b2 );
		System.out.println(String.valueOf(result));
	}

	public static void testXOR(){
		// exclusive or
		byte b = (byte) 0xA4;   // b10100100
		byte b2 = ( byte) 0xA1; // b10100001
		byte result = (byte)( b ^ b2 ); // expect b00000101 , aka 5
		System.out.println( String.valueOf(result));
	}

	public static void main(String[] args){
		testAnd();
		testOr();
		testXOR();
	}
}
