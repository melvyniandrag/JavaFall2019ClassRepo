/**
 * In this example we'll just test out the add, remove, contains and clear methods.
 * boolean add(E e)
 * boolean addAll(Collection<? extends E> c)
 * void clear()
 * boolean    contains(Object o)
 * boolean     containsAll(Collection<?> c)
 * boolean     equals(Object o)
 * int     hashCode()
 * boolean     isEmpty()
 * boolean     remove(Object o)
 * boolean     removeAll(Collection<?> c)
 * default boolean     removeIf(Predicate<? super E> filter)
 * boolean     retainAll(Collection<?> c)
 * int     size()
 */

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class TestCollectionAPI{
    public static void main(String[] args){
        List<Integer> ints = new ArrayList<Integer>(); // feel free to use ArrayList or LinkedList
        boolean successfullyAdded = ints.add(new Integer(1));
     
        List<Integer> intsToAdd = new LinkedList<Integer>();
        intsToAdd.add(new Integer(2)); // you don't need to handle the boolean return val if you don't care about it
        intsToAdd.add(new Integer(3));

        boolean successfullyAddedAll = ints.addAll(intsToAdd);
        
        boolean areArraysEqual = intsToAdd.equals(ints);
        //shouldnt be equal. should be false.
        
        boolean intsContainsOtherArr = ints.containsAll(intsToAdd);
        // should be true.

        boolean intsContainsAThousand = ints.contains(new Integer(1000)); 
        // expect false;

        boolean successfulRemove = ints.remove(new Integer(2));
        System.out.println("Was int removed? Expect true, got: " + successfulRemove);

        boolean successfulRemoveAll = ints.removeAll(intsToAdd); 
        // already removed 2. Now we are trying to remove 2 and 3.
        System.out.println("Was removal successful? Expect true, got: " + successfulRemoveAll);

        boolean isEmpty = ints.isEmpty();
        System.out.println("Is list empty? Expect false, should still contain Integer(1). got: " + isEmpty);

        int hashCode = ints.hashCode();
        System.out.println("This is an encoded form of the list: " + hashCode);

        ints.clear();
        System.out.println("Just cleared list");
        
        boolean isEmptyNow = ints.isEmpty();
        System.out.println("Is list empty now? " + isEmptyNow);

        int size = ints.size();
        System.out.println("Size of list should now be 0. Size is: " + size);                        
    }
}

