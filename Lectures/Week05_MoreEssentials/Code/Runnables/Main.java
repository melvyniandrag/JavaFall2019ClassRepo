public class Main{
	public static void main(String[] args){
		Thread t1 = new Thread( new B() );
		t1.start();
		try{
			t1.join();
		}
		catch(InterruptedException e){
		}
		System.out.println(Thread.currentThread().getName() + " says all done.");
	}
}
