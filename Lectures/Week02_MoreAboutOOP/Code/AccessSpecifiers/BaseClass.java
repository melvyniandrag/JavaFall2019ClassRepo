public class BaseClass {
	public void publicMethod(){
		System.out.println("public method called.");
	}


	// This is visible in main because it is in
	// the 'default package', because nothing is labeled as part of a package. 
	protected void protectedMethod(){
		System.out.println("protected method called");
	}

	private void privateMethod(){
		System.out.println("private method called");
	}

	void noModMethod(){
		System.out.println("Called method without modifier" );
	}
}
