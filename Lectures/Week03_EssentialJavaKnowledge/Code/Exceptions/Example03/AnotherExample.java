public class AnotherExample{
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
