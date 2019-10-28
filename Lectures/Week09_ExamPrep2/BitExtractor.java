public class BitExtractor{
	public static byte getBit(byte b, int index)
	{
	   return (byte)((b >> index) & 0x01);
	}
	
	public static void main(String[] args){
		byte b = (byte)0x55;
		for( int idx = 0; idx < 8; ++idx){
			System.out.println(String.format("%dth bit: %d", idx, getBit(b, idx)));
		}
	}
}
