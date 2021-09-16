import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;

public class MiddleInsertionComparison{
    public static final int INITIAL_ARRAY_SIZE = 100000;
    public static final int N_INSERTIONS = 10000;

    public static void InsertIntoList(List<Integer> list, String listType){
        System.out.println("Starting insertion experiment for: " + listType);
   		long timeStart = System.currentTimeMillis();
		for(int i = 0; i < N_INSERTIONS; ++i ){
            list.add(1,10); // insert the number 1 at index 10
		}
		long timeEnd = System.currentTimeMillis();
        Long duration = new Long(timeEnd - timeStart);
		System.out.println(listType + " took : " + duration.toString() + "milliseconds");
 }

	public static void main(String[] args){
		List<Integer> linkedList = new LinkedList<Integer>(Collections.nCopies(INITIAL_ARRAY_SIZE, 0));
		List<Integer> arrayList  = new ArrayList<Integer>(Collections.nCopies(INITIAL_ARRAY_SIZE, 0));
        InsertIntoList(linkedList, "Linked");
        InsertIntoList(arrayList, "Array");
	}
}
