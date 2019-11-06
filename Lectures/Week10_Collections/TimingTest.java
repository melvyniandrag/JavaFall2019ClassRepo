import java.util.LinkedList;
import java.util.ArrayList;

public class TimingTest{
	public static void main(String[] args){

		ArrayList arrayList = new ArrayList();
		
		LinkedList linkedList = new LinkedList();

		final int N = 1000000;
		final int M = 10000;
		
		// ArrayList add
		
		long startTime = System.nanoTime();
		
		for (int i = 0; i < N; i++) {
		
			arrayList.add(i);
		
		}
		
		long endTime = System.nanoTime();
		
		long duration = endTime - startTime;
		
		System.out.println("ArrayList add:  " + duration);
		
		// LinkedList add
		
		startTime = System.nanoTime();
		
		for (int i = 0; i < N; i++) {
		
			linkedList.add(i);
		
		}
		
		endTime = System.nanoTime();
		
		duration = endTime - startTime;
		
		System.out.println("LinkedList add: " + duration);
		
		// ArrayList get
		
		startTime = System.nanoTime();
		
		for (int i = 0; i < M; i++) {
		
			arrayList.get(i);
		
		}
		
		endTime = System.nanoTime();
		
		duration = endTime - startTime;
		
		System.out.println("ArrayList get:  " + duration);
		
		// LinkedList get
		
		startTime = System.nanoTime();
		
		for (int i = 0; i < M; i++) {
		
			linkedList.get(i);
		
		}
		
		endTime = System.nanoTime();
		
		duration = endTime - startTime;
		
		System.out.println("LinkedList get: " + duration);
		
		// ArrayList remove
		
		startTime = System.nanoTime();
		
		for (int i = M - 1; i >=0; i--) {
		
			arrayList.remove(i);
		
		}
		
		endTime = System.nanoTime();
		
		duration = endTime - startTime;
		
		System.out.println("ArrayList remove:  " + duration);
		
		// LinkedList remove
		
		startTime = System.nanoTime();
		
		for (int i = M - 1; i >=0; i--) {
		
			linkedList.remove(i);
		
		}
		
		endTime = System.nanoTime();
		
		duration = endTime - startTime;
		
		System.out.println("LinkedList remove: " + duration);
		
	}
}
