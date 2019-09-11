public class StaticExample{

	// Question I don't know the answer to - how much flexibility is there in reordering these
	// keywords? Find a reference and demonstrate as many permutations as you can of these keywords.
	// i.e. can you say:
	// static public int final FIVE = 5; // ?
	// I don't know, a good homework idea is for you to find out and let me know.
	// Use google, thumb through some books, ask whatever Java programmers you know. 
	// I want an official refrence and then some java files with different orderings of the words 
	// in which you show what works and what doesn't.
	
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
		
		System.out.println("How many 'StaticExample's? " + StaticExample.NumInstances );
		StaticExample se1 = new StaticExample();
		se1.CantTouchThis(); // But this works.
	
		System.out.println("How many 'StaticExample's? " + StaticExample.NumInstances );
		StaticExample se2 = new StaticExample();
		
		System.out.println("How many 'StaticExample's? " + StaticExample.NumInstances );
		StaticExample se3 = new StaticExample();
	
		System.out.println("How many 'StaticExample's? " + StaticExample.NumInstances );
		


	}
}
