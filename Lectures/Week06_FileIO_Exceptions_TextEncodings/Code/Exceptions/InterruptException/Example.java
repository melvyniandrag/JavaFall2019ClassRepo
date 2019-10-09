public class Example{
	public Thread t1;

	public void functionThatInterrupts(){
		t1.interrupt();
	}

	public static void main(String[] args){
		Example e = new Example();
		e.t1 = new MyThread();
		e.t1.start();
		e.functionThatInterrupts();
		try{
			e.t1.join();
		}
		catch(InterruptedException ie){
			System.err.println("join() interrupted");
		}
	}
}

class MyThread extends Thread{
	public void run(){
		while(true){
			try{
				Thread.sleep(500);
			}
			catch( InterruptedException ie){
				System.err.println("sleep() interruped!");
				return;
			}
		}
	}
}
