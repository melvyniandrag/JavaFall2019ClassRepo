public class EqualsExample{
	public static void main(String[] args){
		Float f1 = new Float(100.679f);
		Float f2 = new Float(100.0f);
		Byte b1 = new Byte(( byte ) 100 );

		boolean f1Eqf2 = f1.equals(f2);
		System.out.println("Does f1 equal f2? " + f1Eqf2 );

		boolean f2Eqb1 = f2.equals(b1);
		System.out.println("Does f2 equal b1? " + f2Eqb1 );
		// you may be surprised by this.
		// As noted in the documentation:
		// https://docs.oracle.com/javase/tutorial/java/data/numberclasses.html
		// " The methods return true if the argument is not null and is an object of the same type and with the same numeric value. "
		// Since Byte and Float don't have the same type, they cannot be considered equal.
	}
}
