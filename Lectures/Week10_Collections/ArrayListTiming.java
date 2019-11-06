import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ArrayListTiming{
	public static void main(String[] args){
		final int N = Integer.parseInt(args[0]);	
		final int ONE = 1;
		List<Integer> list = new ArrayList<Integer>(Collections.nCopies(N, 0));
		long timeStart = System.currentTimeMillis();
		for(int i = 0; i < N; ++i ){
			if(i % 1000 == 0){
				System.out.println("processing iteration: " + i);
			}
			Random rn = new Random();
			//The below should work in Java, but it doesn't. This is unexpected behavior - the mod operator should always return positive.
			//int index = rn.nextInt() % N;
			// To fix the problem do this:
			int index = rn.nextInt() % N;
			if(index < 0){
				index += N;
			}
			// More about the issue:
			//https://stackoverflow.com/questions/5385024/mod-in-java-produces-negative-numbers
			list.add(index, ONE);
		}
		long timeEnd = System.currentTimeMillis();
		System.out.println(timeEnd - timeStart);
			
	}
}
