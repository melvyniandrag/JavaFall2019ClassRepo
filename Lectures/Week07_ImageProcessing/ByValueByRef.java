public class ByValueByRef{
	public static void f( int[][] arr ){
		arr[0][0] = 100;
	}

	public static void main(String[] args){
		int[][] arr = {{1,2},{3,4}};
		f(arr);
		for(int[] row : arr){
			for( int col : row ){
				System.out.print(col + " ");
			}
			System.out.println("");
		}
	}
}

