import java.lang.*;

public class B extends A implements Runnable{
	public void f(){
		for( int i = 0; i < 10; i++){
			System.out.println(Thread.currentThread().getName() + " counted " + i );
		}
	}

	@Override
	public void run(){
		f();
	}
}
