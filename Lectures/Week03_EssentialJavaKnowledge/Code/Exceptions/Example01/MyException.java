public class MyException{
	public static void main(String[] args){
		int[] a = {1, 2, 3};
		try{
			System.out.println(String.valueOf(a[3]));
		}
		catch (Exception e){
			System.out.println("Caught exception!");
			System.out.println(e.getMessage());
		}
	}
}
