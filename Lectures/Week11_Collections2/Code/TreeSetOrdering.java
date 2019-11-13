/**
 * Linked Hash Sets do maintain insertion order! 
 */
import java.util.Set;
import java.util.TreeSet;

public class TreeSetOrdering{
	public static void main(String[] args){
		Set<String> s = new TreeSet<String>();
		boolean b8 = s.add("Haha");
		System.out.println(b8);
		boolean b4 = s.add("World\n");
		System.out.println(b4);
		boolean b5 = s.add("World\n");
		System.out.println(b5);
		boolean b6 = s.add(new String("World\n"));
		System.out.println(b6);

		boolean b1 = s.add(new String("Hello "));
		System.out.println(b1);
		boolean b2 = s.add(new String("Hello "));
		System.out.println(b2);
		boolean b3 = s.add("Hello ");
		System.out.println(b3);

		for( String str : s ){
			System.out.print(str);
		}

		System.out.println(s.toString());

	}
}

