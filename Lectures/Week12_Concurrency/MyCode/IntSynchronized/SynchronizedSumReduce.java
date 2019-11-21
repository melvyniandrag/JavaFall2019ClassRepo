import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class SynchronizedSumReduce implements Runnable{

	public static int total = 0;
	
	private List<Integer> _myInts = null;

	SynchronizedSumReduce( List<Integer> ints ){
		_myInts = ints;
	}

	private synchronized static void updateTotal( int partialSum ){
		total += partialSum;
	}

	public void run(){
		int partialSum = 0;	
		for(Integer i : _myInts ){
			partialSum += i;
		}
		updateTotal(partialSum);
	}

	public static void main(String[] args){
		List<Integer> intList = new ArrayList<Integer>();
		final int N = Integer.parseInt(args[0]);
		for(int i=0; i < N; ++i){
			intList.add(i);
		}
		List<Integer> intList1 = intList.subList(0, N/4); // upperBound is Exclusive
		List<Integer> intList2 = intList.subList(N/4, N/2); // lowerBound is inclusive
		List<Integer> intList3 = intList.subList(N/2, 3*N/4); // lowerBound is inclusive
		List<Integer> intList4 = intList.subList(3*N/4, N); // lowerBound is inclusive
		final long T0 = System.nanoTime();
		Thread t1 = new Thread(new SynchronizedSumReduce(intList1));	
		Thread t2 = new Thread(new SynchronizedSumReduce(intList2));
		Thread t3 = new Thread(new SynchronizedSumReduce(intList3));
		Thread t4 = new Thread(new SynchronizedSumReduce(intList4));
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		try{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		}
	
		catch( Exception ex ){
			System.err.println(ex.toString());
		}
		final long TF = System.nanoTime();
		System.out.println(String.format(
			"The sum of the integers in [%d, %d) is %d.",
			0, N, SynchronizedSumReduce.total )
		);
		System.out.println(String.format(
			"The sum took %d nanoseconds to compute.",
			TF - T0)
		);
	}
}

