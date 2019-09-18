import java.lang.*;

class Parse{
	public static void main(String[] args){
		//String s = "300"; // Doesnt work
		String s = "127"; // works. Do you know why?
		int ten = 10;
		byte b = Byte.parseByte(s, ten);
		System.out.println(String.valueOf(b));
	}
}
