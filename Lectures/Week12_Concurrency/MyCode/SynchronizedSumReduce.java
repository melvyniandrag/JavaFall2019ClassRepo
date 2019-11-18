import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class SynchronizedSumReduce implements Runnable{

	public static int total = 0;
	
	private List<Integer> _myInts = null;

	SynchronizedSumReduce( List<Integer> ints ){
		_myInts = ints;
	}

	private synchronized void updateTotal( int partialSum ){
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
		final int N = 100000;
		for(int i=0; i < N; ++i){
			intList.add(i);
		}
		List<Integer> intList1 = intList.subList(0, N/2); // upperBound is Exclusive
		List<Integer> intList2 = intList.subList(N/2, N); // lowerBound is inclusive
		Thread t1 = new Thread(new SynchronizedSumReduce(intList1));	
		Thread t2 = new Thread(new SynchronizedSumReduce(intList2));
		t1.start();
		t2.start();
		try{
			t1.join();
			t2.join();
		}
		catch( Exception ex ){
			System.err.println(ex.toString());
		}
		System.out.println(SynchronizedSumReduce.total);
	}
}

