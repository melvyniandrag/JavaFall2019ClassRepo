public class NameThoseThreadsSolution
{
   public static void main (String [] args)
   {
      MyThread mt1;
      MyThread mt2;
      MyThread mt3;

      if ( args.length != 3 ){
      	System.out.println("You must provide 3 command line params! You only gave " + args.length);
	return;
      }
      else{
        mt1 = new MyThread (args [0]);
        mt2 = new MyThread (args [1]);
        mt3 = new MyThread ();
      }
      mt1.start ();
      mt2.start ();
      mt3.start ();
   }
}


// A Curiosity
// Up until now I've said put ONE class per file, and name the class the same as the file.
// Note here we have two classes
// But MyThread is not public.
// Make MyThread public and then try to compile. 
// The rule is that you can only have one public class per file, and the public class
// must have the same name as the file.
// ALSO note that a MyThread.class file is generated by the compiler.
// Seperate .class files are made for every class.
class MyThread extends Thread
{
   MyThread ()
   {
   	super("DEFAULT");
   }
   MyThread (String name)
   {
      super (name); // Pass name to Thread superclass
   }
   public void run ()
   {
	   for ( int i = 0; i < 50; i++){
      		System.out.println ("My name is: " + getName() + " My iter is " + i);
	   }
   }
}
