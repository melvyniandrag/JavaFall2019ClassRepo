public class SerialSum{
	public static void main(String[] args){
		final int N = Integer.parseInt(args[0]);
		int total = 0;
		final long T0 = System.nanoTime();
		for( int i = 0; i < N; ++i ){
			total += i;
		}
		final long TF = System.nanoTime();
		System.out.println(String.format(
			"The sum of the integers in [%d, %d) is %d.",
			0, N, total )
		);
		System.out.println(String.format(
			"The sum took %d nanoseconds to compute.",
			TF - T0)
		);
	}
}

