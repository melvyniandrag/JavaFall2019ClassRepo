public class AnotherExample{
	// note that I don't need to put the 'throws' keyword here.
	// Java can't handle this exception, so we don't have to catch it anywhere
	// or mark the method as being one that throws.
	public static int divide( int dividend, int divisor ){
		if( divisor == 0 ){
			throw new IllegalArgumentException("Divisor cannot be zero");
		}
		else{
			return dividend / divisor;
		}

	}

	public static void main(String[] args){
		int dividend = 1;
		int divisor = 0;

		//try{
		       	int quotient = divide( dividend, divisor );
			System.out.println(String.valueOf(quotient));

		//}
		//catch( IllegalArgumentException e ){
		//	System.err.println(e.getMessage());
		//}
	}
}
