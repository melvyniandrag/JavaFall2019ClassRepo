package singletonpackage;

public class SingletonExample{
	
	private static SingletonExample s = null;
	
	public static int i;

	private SingletonExample(){
		i = 1;
	}
	
	public static SingletonExample getInstance(){
		if( s == null ){
			s = new SingletonExample();
		}
		return s;
	}
}
