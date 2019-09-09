public class A {
	public void dog( int i )
	{
		switch(i)
		{
			case 1:
				System.out.println("Woof");
				break;
			case 2:
				System.out.println("Arf");
				break;
			default:
				System.out.println("Wag");
		}
	}

	public void cat( int numMeows ){
		for ( int i = 0; i < numMeows; i++){
			sayMeow();
		}
	}

	public  void sayMeow(){
		System.out.println("Meow");
	}

	public static void main(String[] args){
		A a = new A();
		a.cat(5);
	}
}
