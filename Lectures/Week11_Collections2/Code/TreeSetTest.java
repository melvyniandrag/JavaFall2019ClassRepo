import java.util.Set;
import java.util.TreeSet;

public class TreeSetTest{
	public static void main(String[] args){
		Set<String> s = new TreeSet<String>();
		s.add(new String("Hello "));
		s.add(new String("Hello "));
		s.add("Hello ");

		s.add("World\n");
		s.add("World\n");
		s.add(new String("World\n"));

		for( String str : s ){
			System.out.print(str);
		}

		System.out.println(s.toString());

	}
}

