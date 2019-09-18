import packagename.FinalClass;

public class Main{
	public static void main(String[] args){
		FinalClass fc = new FinalClass();
		System.out.println(String.valueOf(fc.getIntOne()));
		fc.myMethod();
		fc.finalMethod();
	}
}
