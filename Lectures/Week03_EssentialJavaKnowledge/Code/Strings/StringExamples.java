public class StringExamples{
	public static void main(String[] args){
		String s1 = "Hello";
		String s2 = " ";
		String s3 = "World";
		String s4 = s1.concat(s2).concat(s3);
		System.out.format("%d. %s%n", 1, s4);
	
	
		//Byte b = String.valueOf("126"); // maybe impossible
		Byte b = new Byte((byte)0x02);
		String fs = String.format("The value of Byte b is %b%n", b);
		System.out.print(fs);
	}
}

