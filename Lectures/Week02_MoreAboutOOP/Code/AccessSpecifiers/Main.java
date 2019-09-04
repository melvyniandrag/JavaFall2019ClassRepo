public class Main{
	public static void main(String[] args){
		BaseClass bc = new BaseClass();
		bc.publicMethod(); // works
		bc.protectedMethod(); // doesn't work - protected method can't be called from Main.
		//bc.privateMethod();
		bc.noModMethod();


		InheritClass ic = new InheritClass();
		ic.publicMethod();
		ic.protectedMethod();
		ic.noModMethod(); // note that we can still acess this method here because we are in the same package. When we get to packages you can experiment more with this and then you'll see some new behavior.
		System.out.println(String.valueOf(ic.selectivelyOutgoing));
		System.out.println(String.valueOf(ic.nothidden));
		System.out.println(String.valueOf(ic.unspecified));
		
		// Can't do this
		//System.out.println(String.valueOf(ic.secret));
	}
}
