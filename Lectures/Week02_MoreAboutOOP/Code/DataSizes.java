public class DataSizes{
	public static void main(String[] args){
		System.out.println("Size of int = " + Integer.SIZE + " bits");	
		System.out.println("Size of float = " + Float.SIZE + " bits");	
		System.out.println("Size of double = " + Double.SIZE + " bits");	
		System.out.println("Size of short = " + Short.SIZE + " bits");	
		System.out.println("Size of long = " + Long.SIZE + " bits");	
		System.out.println("Size of byte = " + Byte.SIZE + " bits");	
		System.out.println("Size of char = " + Character.SIZE + " bits");	

		System.out.println("\n\nNow Look what happens when we take a datatype and");
		System.out.println("put a data type too large into it.");
		
		int i = Integer.MAX_VALUE;
		System.out.println("The max value of an int is: " + i );
		System.out.println("Now when we add one to that and store it in an integer we get ... ");
		i = i + 1;
		System.out.println("Integer.MAX_VALUE + 1 = " + i );

		System.out.println("Now the same experiment with a byte.");
		byte b = Byte.MAX_VALUE;
		System.out.println("The max value of a byte is " + b);
		b = (byte)(b + 1);
		System.out.println("Now that we add one to that we get: " + b );
	}
}
