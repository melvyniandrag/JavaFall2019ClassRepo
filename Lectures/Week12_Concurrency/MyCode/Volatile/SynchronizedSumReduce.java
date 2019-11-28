import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class SynchronizedSumReduce implements Runnable{
	// Note that total is a volatile variable
	public static volatile long total = 0;
	public static int numUpdateCalls = 0;
	
	private List<Integer> _myInts = null;

	SynchronizedSumReduce( List<Integer> ints ){
		_myInts = ints;
	}
	
	// since total is volatile, this method is not synchronized.
	private void updateTotal( long partialSum ){
		total += partialSum;
		numUpdateCalls++;
	}

	public void run(){
		long partialSum = 0;	
		for(Integer i : _myInts ){
			partialSum += i;
		}
		updateTotal(partialSum);
	}

	public static void main(String[] args) throws Exception{
		List<Integer> intList1 = new ArrayList<Integer>();
		List<Integer> intList2 = new ArrayList<Integer>();
		List<Integer> intList3 = new ArrayList<Integer>();
		List<Integer> intList4 = new ArrayList<Integer>();
		List<Integer> intList5 = new ArrayList<Integer>();
		List<Integer> intList6 = new ArrayList<Integer>();
		List<Integer> intList7 = new ArrayList<Integer>();
		List<Integer> intList8 = new ArrayList<Integer>();
		final int N = Integer.parseInt(args[0]);
		for(int i=0; i < N/8; ++i){
			intList1.add(i);
		}
		for(int i=N/8; i < N/4; ++i){
			intList2.add(i);
		}
		for(int i=N/4; i < 3*N/8; ++i){
			intList3.add(i);
		}
		for(int i=3*N/8; i < N/2; ++i){
			intList4.add(i);
		}
		for(int i=N/2; i < 5*N/8; ++i){
			intList5.add(i);
		}
		for(int i=5*N/8; i < 3*N/4; ++i){
			intList6.add(i);
		}
		for(int i=3*N/4; i < 7*N/8; ++i){
			intList7.add(i);
		}
		for(int i=7*N/8; i < N; ++i){
			intList8.add(i);
		}
		Thread t1 = new Thread(new SynchronizedSumReduce(intList1));
		Thread t2 = new Thread(new SynchronizedSumReduce(intList2));
		Thread t3 = new Thread(new SynchronizedSumReduce(intList3));
		Thread t4 = new Thread(new SynchronizedSumReduce(intList4));
		Thread t5 = new Thread(new SynchronizedSumReduce(intList5));
		Thread t6 = new Thread(new SynchronizedSumReduce(intList6));
		Thread t7 = new Thread(new SynchronizedSumReduce(intList7));
		Thread t8 = new Thread(new SynchronizedSumReduce(intList8));
		final long T0 = System.nanoTime();
		long TF = 0;
		Thread.sleep(1000);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
	
		try{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
			TF = System.nanoTime();
		}
		catch( Exception ex ){
			System.err.println(ex.toString());
		}
		System.out.println(String.format(
			"The sum of the integers in [%d, %d) is %d.",
			0, N, SynchronizedSumReduce.total )
		);
		System.out.println(String.format(
			"The sum took %d nanoseconds to compute.",
			TF - T0)
		);
		System.out.println(String.format(
			"Made %d calls to updateTotal()",
			SynchronizedSumReduce.numUpdateCalls)
		);
	}
}

