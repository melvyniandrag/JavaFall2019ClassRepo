public class StaticExample{

	public final static int FIVE = 5;

	public static int NumInstances = 0;

	StaticExample(){
		// Instances can access static vars though. That's cool.
		NumInstances++;
	}

	public static void HelloWorld(){
		System.out.println("You can access me from main without an instance of 'StaticExample' because I'm static.");
	}

	public void CantTouchThis(){
		System.out.println("Dun dun dun dun");
	}

	public static void main(String[] args){
		System.out.println(String.valueOf(FIVE));
		HelloWorld();


		// CantTouchThis(); // Just try.
		// You cant run this because it isn't a static method - non static methods need to be
		// called from an instance of a class like instance.method(). See how we call
		// se1.CantTouchThis() below. If we made the method static, then we could call it here without
		// and instance of the class.
		
		System.out.println("How many 'StaticExamples? " + StaticExample.NumInstances );
		StaticExample se1 = new StaticExample();
		se1.CantTouchThis(); // But this works.
	
		System.out.println("How many 'StaticExamples? " + StaticExample.NumInstances );
		StaticExample se2 = new StaticExample();
		
		System.out.println("How many 'StaticExamples? " + StaticExample.NumInstances );
		StaticExample se3 = new StaticExample();
	
		System.out.println("How many 'StaticExamples? " + StaticExample.NumInstances );
		


	}
}
