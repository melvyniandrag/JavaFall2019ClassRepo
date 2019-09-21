public class MyException{
	public static void main(String[] args){
		int[] a = {1, 2, 3};
		try{
			System.out.println(String.valueOf(a[3]));
		}
		catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Caught ArrayIndexOutOfBoundsException!");
			System.out.println(e.getMessage());
		}
		catch( Exception e ){
			System.out.println("Caught other exception!");
		}
	}
}
