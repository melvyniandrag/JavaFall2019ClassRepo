public class ByValueByRef{

	public static void printTestHeader(String s){
		System.out.println("\n\n*****************************************************************************");
		System.out.println(s);
		System.out.println("*****************************************************************************");
	}

	public static void f( int[][] arr ){
		arr[0][0] = 100;
	}

	public static void TestIntArr(){
		printTestHeader("Are int arrays passed to void functions changed in the functions they are passed to?");
		int[][] arr = {{1,2},{3,4}};
		System.out.println("Array before going to function");

		for(int[] row : arr){
			for( int col : row ){
				System.out.print(col + " ");
			}
			System.out.println("");
		}
		
		f(arr);
		
		System.out.println("Array after going to function");
		for(int[] row : arr){
			for( int col : row ){
				System.out.print(col + " ");
			}
			System.out.println("");
		}
	}

	// Do this in class
	public static void TestInt(){
		// Create an int, pass it to a function that changes it's value, then 
	}

	// Do this in class
	public static void TestObject(){
		// Create a 'Container' object adn modify it in a function.
		// After leaving the function has the object changed?
	}

	public static void main(String[] args){
		TestInt();
		TestIntArr();
		TestObject();
	}
}


class Container{
	public int x;
	
	Container(int i){ x = i;}
}
