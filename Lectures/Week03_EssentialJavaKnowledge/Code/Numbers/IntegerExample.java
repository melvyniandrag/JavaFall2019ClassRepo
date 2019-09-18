public class IntegerExample{
	public static void main(String[] args){
		Integer i = new Integer(1); // construct an integer.
		// Integer is like int, but it is a class and as such has a constructor.
		// All objects ( classes ) have constructors.
		byte b = i.byteValue();
		System.out.println("The byte value of i is " + String.valueOf(b));

		float f = i.floatValue();
		System.out.println("The float value of i is " + String.valueOf(f));
		
		// The above things are all as expected.


		// An interesting thing happens here:
		Integer j = new Integer(129);
		byte b2 = j.byteValue();
		System.out.println("The byte value of j is " + String.valueOf(b2));

		// Why did this happen? A hint:
		System.out.println("The max value a byte can hold is " + Byte.MAX_VALUE);
	}
}
