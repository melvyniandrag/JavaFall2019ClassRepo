import packagename.FinalClass;
import packagename.anotherlevel.Constants;
public class Main{
	public static void main(String[] args){
		FinalClass fc = new FinalClass();
		System.out.println(String.valueOf(fc.getIntOne()));
		fc.myMethod();
		fc.finalMethod();
		
		System.out.println("A look at constants");
		System.out.println(String.valueOf(Constants.ONE)); 
		// Note the above. The java compiler can see the current working directory. 
		// So we can either import things we need from other directories like
		// `import packagename.FinalClass`
		// or we can just use stuff in subdirectories like
		// `packagename.anotherlevel.Constants.ONE
	}
}
