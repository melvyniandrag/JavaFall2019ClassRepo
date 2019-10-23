public class LeftShifter implements Shifter{
	public int myInt = 0xFF6F8F90;

	public void shift(int ExpectedValue, int shift){
		final int shifted = ( myInt >> shift);
		System.out.println( shifted == ExpectedValue );
	}	
	
	public void runAllFunctions(){
		shift(0x00, 1);  // TODO change 0x00 to the proper value
		shift(0x00, 10); // TODO change 0x00 to the proper value
		shift(0x00, 20); // TODO change 0x00 to the proper value
	}
}

