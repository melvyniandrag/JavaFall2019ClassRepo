public class SignedBitshift{
	public static void main(String[] args){
		System.out.println("The Easy Stuff!!");
		byte b = (byte) 1;
		String b_String = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');;
		System.out.println(String.valueOf(b) + " " + b_String);
		byte b2 = (byte) (b >> 1);
		String b2_String = String.format("%8s", Integer.toBinaryString(b2)).replace(' ', '0');
		System.out.println(String.valueOf(b2) + " " + b2_String);
		byte b3 = (byte)( b << 7 );
		String b3_String = String.format("%8s", Integer.toBinaryString(b3));
		System.out.println(String.valueOf(b3) + " " + b3_String);



		System.out.println("\r\nThe Hard Stuff!! ( Part 1 )");
		byte nb = (byte) -1;
		String nb_String = String.format("%8s", Integer.toBinaryString(nb & 0xff));
		System.out.println( String.valueOf(nb) + " " + nb_String);
	}
}
