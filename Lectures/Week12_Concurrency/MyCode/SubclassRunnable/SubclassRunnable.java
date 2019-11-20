public class SubclassRunnable implements Runnable {
	private static int numThreads = 0;
	
	private int threadNum;

	SubclassRunnable(){
		threadNum = numThreads++;
	}

	public void run(){
			System.out.println(String.format("Hello from thread %d", this.threadNum));
	}

	public static void main(String[] args){
		Thread t0 = new Thread(new SubclassRunnable());
		Thread t1 = new Thread(new SubclassRunnable());
		Thread t2 = new Thread(new SubclassRunnable());
		Thread t3 = new Thread(new SubclassRunnable());
		Thread t4 = new Thread(new SubclassRunnable());
		t0.start();
		t1.start();
		t2.start();
		t3.start();
		t4.start();	
	}
}

