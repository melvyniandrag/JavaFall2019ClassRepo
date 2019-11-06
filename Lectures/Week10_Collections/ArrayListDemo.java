import java.util.ArrayList;

public class ArrayListDemo{
	public static void main(String[] args){
		ArrayList<Integer> intArrayList = new ArrayList<Integer>();
		intArrayList.add(1);
		intArrayList.add(100);
		for( int i : intArrayList ){
			System.out.println(i);
		}

		ArrayList<MyClass> classList = new ArrayList<MyClass>();
		classList.add( new MyClass() );
		classList.add( new MyClass() );
		classList.add( new MyClass() );

		for( MyClass mc : classList ){
			System.out.println( mc.x );
		}
	}
}

class MyClass{
	public int x = 1;
}
