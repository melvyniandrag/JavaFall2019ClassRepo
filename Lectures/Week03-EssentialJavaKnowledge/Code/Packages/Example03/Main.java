import packagename.FinalClass;

public class Main{
	public static void main(String[] args){
		FinalClass fc = new FinalClass();
		System.out.println(String.valueOf(fc.getIntOne()));
		fc.myMethod();
		fc.finalMethod();
		
		System.out.println("A look at constants");
		// Note how we have two packages with file 'Constants.ONE'.
		// Java understands the difference between them and prints the corresponding value 
		// from each.
		System.out.println(String.valueOf(packagename.anotherlevel.Constants.ONE)); 

		System.out.println(String.valueOf(packagename.adifferentotherlevel.Constants.ONE)); 
	}
}
