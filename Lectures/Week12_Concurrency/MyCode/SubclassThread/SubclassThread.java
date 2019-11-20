public class SubclassThread extends Thread {
	private static int numThreads = 0;
	
	private int threadNum;

	SubclassThread(){
		threadNum = numThreads++;
	}

	public void run(){
			System.out.println(String.format("Hello from thread %d", this.threadNum));
	}

	public static void main(String[] args){
        Thread t0 = new SubclassThread();
        Thread t1 = new SubclassThread();
        Thread t2 = new SubclassThread();
        Thread t3 = new SubclassThread();
        Thread t4 = new SubclassThread();
		t0.start();
		t1.start();
		t2.start();
		t3.start();
		t4.start();	
	}
}

