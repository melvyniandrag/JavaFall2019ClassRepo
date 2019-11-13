import java.util.Set;
import java.util.LinkedHashSet;

public class LinkedHashSetTest{
	public static void main(String[] args){
		Set<String> s = new LinkedHashSet<String>();
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

