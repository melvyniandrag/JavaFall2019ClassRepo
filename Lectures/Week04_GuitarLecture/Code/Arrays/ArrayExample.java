import java.util.Arrays;

public class ArrayExample{
	public static void main(String[] args){
		int[] intArr1 = {1, 2, 3};
		int[] intArr2 = {1, 2, 3};
		System.out.println("Compare the hashcodes of two arrays");
		int hashCode1 = Arrays.hashCode(intArr1);
		int hashCode2 = Arrays.hashCode(intArr2);
		System.out.println(String.valueOf(hashCode1));
		System.out.println(String.valueOf(hashCode2));

		System.out.println("Sort an array in place");
		long[] longArr = {10, 7, 100, 1};
		printArrayContents(longArr);
		Arrays.sort(longArr);	
		printArrayContents(longArr);

		System.out.println("Example of fill()");
		printArrayContents(longArr);
		Arrays.fill(longArr, 0);
		printArrayContents(longArr);
		Arrays.fill(longArr, 0, 1, 1000);

		printArrayContents(longArr);
		
		System.out.println("Example of copyOf");
		int[] intArr = { 1, 2, 3};
		int[] sameArr = intArr;
		int[] copyArr = Arrays.copyOf(intArr, intArr.length);
		System.out.println(String.valueOf(intArr == sameArr));
		System.out.println(String.valueOf(intArr == copyArr));
		printArrayContents(intArr);
		sameArr[0] = 1000;
		printArrayContents(intArr); // should have been changed since sameArr and intArr are the same object.
		copyArr[0] = 1001;
		printArrayContents(intArr); // should not have changed since copyArr and intArr are not hte same object!
	}

	public static void printArrayContents(long[] array){
		for( long elem : array){
			System.out.print(" " + elem);
		}
		System.out.println("");
	}
	
	public static void printArrayContents(int[] array){
		for( long elem : array){
			System.out.print(" " + elem);
		}
		System.out.println("");
	}
}
